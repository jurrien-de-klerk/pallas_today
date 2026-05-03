import 'package:dio/dio.dart';

import '../config/api_config.dart';

/// Holds the authenticated session and provides login/logout operations.
class AuthService {
  AuthService._();

  static final AuthService instance = AuthService._();

  /// Shared Dio used for all Keycloak token requests.
  final Dio _authDio = Dio(
    BaseOptions(
      connectTimeout: const Duration(seconds: 10),
      sendTimeout: const Duration(seconds: 10),
      receiveTimeout: const Duration(seconds: 10),
    ),
  );

  String? _accessToken;
  String? _refreshToken;
  DateTime? _tokenExpiry;

  /// In-flight refresh, shared across concurrent callers.
  Future<void>? _refreshFuture;

  /// The current access token, or null if not authenticated.
  String? get accessToken => _accessToken;

  bool get isAuthenticated => _accessToken != null;

  bool get _isExpired =>
      _tokenExpiry != null &&
      DateTime.now().isAfter(
        _tokenExpiry!.subtract(const Duration(seconds: 30)),
      );

  /// Authenticates [username] and [password] against Keycloak.
  ///
  /// Throws a [LoginException] when the credentials are invalid or the
  /// server is unreachable.
  Future<void> login(String username, String password) async {
    try {
      final response = await _authDio.post(
        '$keycloakBaseUrl/realms/$keycloakRealm/protocol/openid-connect/token',
        data: {
          'grant_type': 'password',
          'client_id': keycloakClientId,
          'username': username,
          'password': password,
        },
        options: Options(contentType: 'application/x-www-form-urlencoded'),
      );
      _applyTokenResponse(response.data as Map<String, dynamic>);
    } on DioException catch (e) {
      final status = e.response?.statusCode;
      final error = e.response?.data is Map
          ? (e.response!.data as Map)['error']
          : null;
      if (status == 400 && error == 'invalid_grant') {
        throw LoginException('Invalid username or password.');
      }
      throw LoginException('Could not reach authentication server.');
    }
  }

  /// Refreshes the access token if it is expired or about to expire.
  ///
  /// Concurrent callers share a single in-flight refresh request — only one
  /// HTTP call is made regardless of how many callers invoke this at once.
  ///
  /// If no refresh token is available, or the refresh fails, the session is
  /// cleared so the app can redirect to the login screen.
  Future<void> refreshIfNeeded() {
    if (!_isExpired) return Future.value();
    // Reuse an in-flight refresh instead of starting a parallel one.
    return _refreshFuture ??= _doRefresh().whenComplete(() {
      _refreshFuture = null;
    });
  }

  Future<void> _doRefresh() async {
    final token = _refreshToken;
    if (token == null) {
      logout();
      return;
    }
    try {
      final response = await _authDio.post(
        '$keycloakBaseUrl/realms/$keycloakRealm/protocol/openid-connect/token',
        data: {
          'grant_type': 'refresh_token',
          'client_id': keycloakClientId,
          'refresh_token': token,
        },
        options: Options(contentType: 'application/x-www-form-urlencoded'),
      );
      _applyTokenResponse(response.data as Map<String, dynamic>);
    } on DioException {
      logout();
    }
  }

  void _applyTokenResponse(Map<String, dynamic> data) {
    _accessToken = data['access_token'] as String;
    _refreshToken = data['refresh_token'] as String?;
    final expiresIn = data['expires_in'] as int? ?? 300;
    _tokenExpiry = DateTime.now().add(Duration(seconds: expiresIn));
  }

  void logout() {
    _accessToken = null;
    _refreshToken = null;
    _tokenExpiry = null;
    _refreshFuture = null;
  }
}

/// Thrown when [AuthService.login] fails.
class LoginException implements Exception {
  const LoginException(this.message);

  final String message;

  @override
  String toString() => message;
}
