import 'package:dio/dio.dart';

import '../config/api_config.dart';

/// Holds the authenticated session and provides login/logout operations.
class AuthService {
  AuthService._();

  static final AuthService instance = AuthService._();

  String? _accessToken;

  /// The current access token, or null if not authenticated.
  String? get accessToken => _accessToken;

  bool get isAuthenticated => _accessToken != null;

  /// Authenticates [username] and [password] against Keycloak.
  ///
  /// Throws a [LoginException] when the credentials are invalid or the
  /// server is unreachable.
  Future<void> login(String username, String password) async {
    final dio = Dio();
    try {
      final response = await dio.post(
        '$keycloakBaseUrl/realms/$keycloakRealm/protocol/openid-connect/token',
        data: {
          'grant_type': 'password',
          'client_id': keycloakClientId,
          'username': username,
          'password': password,
        },
        options: Options(contentType: 'application/x-www-form-urlencoded'),
      );
      _accessToken = response.data['access_token'] as String;
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

  void logout() {
    _accessToken = null;
  }
}

/// Thrown when [AuthService.login] fails.
class LoginException implements Exception {
  const LoginException(this.message);

  final String message;

  @override
  String toString() => message;
}
