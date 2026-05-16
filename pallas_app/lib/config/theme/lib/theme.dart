import "package:flutter/material.dart";

class MaterialTheme {
  final TextTheme textTheme;

  const MaterialTheme(this.textTheme);

  static ColorScheme lightScheme() {
    return const ColorScheme(
      brightness: Brightness.light,
      primary: Color(0xff144740),
      surfaceTint: Color(0xff37675e),
      onPrimary: Color(0xffffffff),
      primaryContainer: Color(0xff2f5f57),
      onPrimaryContainer: Color(0xffa5d7cc),
      secondary: Color(0xff665e49),
      onSecondary: Color(0xffffffff),
      secondaryContainer: Color(0xffe8dcc2),
      onSecondaryContainer: Color(0xff69604c),
      tertiary: Color(0xff516143),
      onTertiary: Color(0xffffffff),
      tertiaryContainer: Color(0xff6a7a5a),
      onTertiaryContainer: Color(0xfff9ffec),
      error: Color(0xff994436),
      onError: Color(0xffffffff),
      errorContainer: Color(0xffb85c4c),
      onErrorContainer: Color(0xff130000),
      surface: Color(0xfffcf8f7),
      onSurface: Color(0xff1c1b1b),
      onSurfaceVariant: Color(0xff484740),
      outline: Color(0xff79776f),
      outlineVariant: Color(0xffc9c6bd),
      shadow: Color(0xff000000),
      scrim: Color(0xff000000),
      inverseSurface: Color(0xff313030),
      inversePrimary: Color(0xff9fd0c6),
      primaryFixed: Color(0xffbaede2),
      onPrimaryFixed: Color(0xff00201c),
      primaryFixedDim: Color(0xff9fd0c6),
      onPrimaryFixedVariant: Color(0xff1d4f47),
      secondaryFixed: Color(0xffeee1c7),
      onSecondaryFixed: Color(0xff211b0b),
      secondaryFixedDim: Color(0xffd1c5ac),
      onSecondaryFixedVariant: Color(0xff4e4633),
      tertiaryFixed: Color(0xffd7e8c2),
      onTertiaryFixed: Color(0xff121f07),
      tertiaryFixedDim: Color(0xffbbcca8),
      onTertiaryFixedVariant: Color(0xff3d4b2f),
      surfaceDim: Color(0xffddd9d8),
      surfaceBright: Color(0xfffcf8f7),
      surfaceContainerLowest: Color(0xffffffff),
      surfaceContainerLow: Color(0xfff7f3f2),
      surfaceContainer: Color(0xfff1edec),
      surfaceContainerHigh: Color(0xffebe7e6),
      surfaceContainerHighest: Color(0xffe5e2e1),
    );
  }

  ThemeData light() {
    return theme(lightScheme());
  }

  static ColorScheme lightMediumContrastScheme() {
    return const ColorScheme(
      brightness: Brightness.light,
      primary: Color(0xff063d36),
      surfaceTint: Color(0xff37675e),
      onPrimary: Color(0xffffffff),
      primaryContainer: Color(0xff2f5f57),
      onPrimaryContainer: Color(0xfff2fffb),
      secondary: Color(0xff3c3623),
      onSecondary: Color(0xffffffff),
      secondaryContainer: Color(0xff756c57),
      onSecondaryContainer: Color(0xffffffff),
      tertiary: Color(0xff2c3a20),
      onTertiary: Color(0xffffffff),
      tertiaryContainer: Color(0xff627253),
      onTertiaryContainer: Color(0xffffffff),
      error: Color(0xff651d12),
      onError: Color(0xffffffff),
      errorContainer: Color(0xffac5343),
      onErrorContainer: Color(0xffffffff),
      surface: Color(0xfffcf8f7),
      onSurface: Color(0xff111111),
      onSurfaceVariant: Color(0xff373630),
      outline: Color(0xff54534b),
      outlineVariant: Color(0xff6f6d65),
      shadow: Color(0xff000000),
      scrim: Color(0xff000000),
      inverseSurface: Color(0xff313030),
      inversePrimary: Color(0xff9fd0c6),
      primaryFixed: Color(0xff46766d),
      onPrimaryFixed: Color(0xffffffff),
      primaryFixedDim: Color(0xff2d5d55),
      onPrimaryFixedVariant: Color(0xffffffff),
      secondaryFixed: Color(0xff756c57),
      onSecondaryFixed: Color(0xffffffff),
      secondaryFixedDim: Color(0xff5c5440),
      onSecondaryFixedVariant: Color(0xffffffff),
      tertiaryFixed: Color(0xff627253),
      onTertiaryFixed: Color(0xffffffff),
      tertiaryFixedDim: Color(0xff4a593c),
      onTertiaryFixedVariant: Color(0xffffffff),
      surfaceDim: Color(0xffc9c6c5),
      surfaceBright: Color(0xfffcf8f7),
      surfaceContainerLowest: Color(0xffffffff),
      surfaceContainerLow: Color(0xfff7f3f2),
      surfaceContainer: Color(0xffebe7e6),
      surfaceContainerHigh: Color(0xffe0dcdb),
      surfaceContainerHighest: Color(0xffd4d1d0),
    );
  }

  ThemeData lightMediumContrast() {
    return theme(lightMediumContrastScheme());
  }

  static ColorScheme lightHighContrastScheme() {
    return const ColorScheme(
      brightness: Brightness.light,
      primary: Color(0xff00332c),
      surfaceTint: Color(0xff37675e),
      onPrimary: Color(0xffffffff),
      primaryContainer: Color(0xff205149),
      onPrimaryContainer: Color(0xffffffff),
      secondary: Color(0xff322c1a),
      onSecondary: Color(0xffffffff),
      secondaryContainer: Color(0xff504935),
      onSecondaryContainer: Color(0xffffffff),
      tertiary: Color(0xff223017),
      onTertiary: Color(0xffffffff),
      tertiaryContainer: Color(0xff3f4e31),
      onTertiaryContainer: Color(0xffffffff),
      error: Color(0xff57130a),
      onError: Color(0xffffffff),
      errorContainer: Color(0xff7e3023),
      onErrorContainer: Color(0xffffffff),
      surface: Color(0xfffcf8f7),
      onSurface: Color(0xff000000),
      onSurfaceVariant: Color(0xff000000),
      outline: Color(0xff2d2c26),
      outlineVariant: Color(0xff4a4942),
      shadow: Color(0xff000000),
      scrim: Color(0xff000000),
      inverseSurface: Color(0xff313030),
      inversePrimary: Color(0xff9fd0c6),
      primaryFixed: Color(0xff205149),
      onPrimaryFixed: Color(0xffffffff),
      primaryFixedDim: Color(0xff013a33),
      onPrimaryFixedVariant: Color(0xffffffff),
      secondaryFixed: Color(0xff504935),
      onSecondaryFixed: Color(0xffffffff),
      secondaryFixedDim: Color(0xff393220),
      onSecondaryFixedVariant: Color(0xffffffff),
      tertiaryFixed: Color(0xff3f4e31),
      onTertiaryFixed: Color(0xffffffff),
      tertiaryFixedDim: Color(0xff29371d),
      onTertiaryFixedVariant: Color(0xffffffff),
      surfaceDim: Color(0xffbbb8b7),
      surfaceBright: Color(0xfffcf8f7),
      surfaceContainerLowest: Color(0xffffffff),
      surfaceContainerLow: Color(0xfff4f0ef),
      surfaceContainer: Color(0xffe5e2e1),
      surfaceContainerHigh: Color(0xffd7d4d3),
      surfaceContainerHighest: Color(0xffc9c6c5),
    );
  }

  ThemeData lightHighContrast() {
    return theme(lightHighContrastScheme());
  }

  static ColorScheme darkScheme() {
    return const ColorScheme(
      brightness: Brightness.dark,
      primary: Color(0xff9fd0c6),
      surfaceTint: Color(0xff9fd0c6),
      onPrimary: Color(0xff003731),
      primaryContainer: Color(0xff2f5f57),
      onPrimaryContainer: Color(0xffa5d7cc),
      secondary: Color(0xfffff9f1),
      onSecondary: Color(0xff36301e),
      secondaryContainer: Color(0xffe8dcc2),
      onSecondaryContainer: Color(0xff69604c),
      tertiary: Color(0xffbbcca8),
      onTertiary: Color(0xff27341a),
      tertiaryContainer: Color(0xff869675),
      onTertiaryContainer: Color(0xff040f00),
      error: Color(0xffffb4a6),
      onError: Color(0xff5d180d),
      errorContainer: Color(0xffb85c4c),
      onErrorContainer: Color(0xff130000),
      surface: Color(0xff141313),
      onSurface: Color(0xffe5e2e1),
      onSurfaceVariant: Color(0xffc9c6bd),
      outline: Color(0xff939188),
      outlineVariant: Color(0xff484740),
      shadow: Color(0xff000000),
      scrim: Color(0xff000000),
      inverseSurface: Color(0xffe5e2e1),
      inversePrimary: Color(0xff37675e),
      primaryFixed: Color(0xffbaede2),
      onPrimaryFixed: Color(0xff00201c),
      primaryFixedDim: Color(0xff9fd0c6),
      onPrimaryFixedVariant: Color(0xff1d4f47),
      secondaryFixed: Color(0xffeee1c7),
      onSecondaryFixed: Color(0xff211b0b),
      secondaryFixedDim: Color(0xffd1c5ac),
      onSecondaryFixedVariant: Color(0xff4e4633),
      tertiaryFixed: Color(0xffd7e8c2),
      onTertiaryFixed: Color(0xff121f07),
      tertiaryFixedDim: Color(0xffbbcca8),
      onTertiaryFixedVariant: Color(0xff3d4b2f),
      surfaceDim: Color(0xff141313),
      surfaceBright: Color(0xff3a3938),
      surfaceContainerLowest: Color(0xff0e0e0e),
      surfaceContainerLow: Color(0xff1c1b1b),
      surfaceContainer: Color(0xff201f1f),
      surfaceContainerHigh: Color(0xff2a2a29),
      surfaceContainerHighest: Color(0xff353434),
    );
  }

  ThemeData dark() {
    return theme(darkScheme());
  }

  static ColorScheme darkMediumContrastScheme() {
    return const ColorScheme(
      brightness: Brightness.dark,
      primary: Color(0xffb4e6dc),
      surfaceTint: Color(0xff9fd0c6),
      onPrimary: Color(0xff002b26),
      primaryContainer: Color(0xff6a9a91),
      onPrimaryContainer: Color(0xff000000),
      secondary: Color(0xfffff9f1),
      onSecondary: Color(0xff36301e),
      secondaryContainer: Color(0xffe8dcc2),
      onSecondaryContainer: Color(0xff4b4431),
      tertiary: Color(0xffd1e2bc),
      onTertiary: Color(0xff1c2911),
      tertiaryContainer: Color(0xff869675),
      onTertiaryContainer: Color(0xff000000),
      error: Color(0xffffd2ca),
      onError: Color(0xff4e0d05),
      errorContainer: Color(0xffd87563),
      onErrorContainer: Color(0xff000000),
      surface: Color(0xff141313),
      onSurface: Color(0xffffffff),
      onSurfaceVariant: Color(0xffdfdcd3),
      outline: Color(0xffb4b2a9),
      outlineVariant: Color(0xff929088),
      shadow: Color(0xff000000),
      scrim: Color(0xff000000),
      inverseSurface: Color(0xffe5e2e1),
      inversePrimary: Color(0xff1e5048),
      primaryFixed: Color(0xffbaede2),
      onPrimaryFixed: Color(0xff001511),
      primaryFixedDim: Color(0xff9fd0c6),
      onPrimaryFixedVariant: Color(0xff063d36),
      secondaryFixed: Color(0xffeee1c7),
      onSecondaryFixed: Color(0xff161103),
      secondaryFixedDim: Color(0xffd1c5ac),
      onSecondaryFixedVariant: Color(0xff3c3623),
      tertiaryFixed: Color(0xffd7e8c2),
      onTertiaryFixed: Color(0xff081402),
      tertiaryFixedDim: Color(0xffbbcca8),
      onTertiaryFixedVariant: Color(0xff2c3a20),
      surfaceDim: Color(0xff141313),
      surfaceBright: Color(0xff454444),
      surfaceContainerLowest: Color(0xff070707),
      surfaceContainerLow: Color(0xff1e1d1d),
      surfaceContainer: Color(0xff282827),
      surfaceContainerHigh: Color(0xff333232),
      surfaceContainerHighest: Color(0xff3e3d3d),
    );
  }

  ThemeData darkMediumContrast() {
    return theme(darkMediumContrastScheme());
  }

  static ColorScheme darkHighContrastScheme() {
    return const ColorScheme(
      brightness: Brightness.dark,
      primary: Color(0xffc7faef),
      surfaceTint: Color(0xff9fd0c6),
      onPrimary: Color(0xff000000),
      primaryContainer: Color(0xff9bccc2),
      onPrimaryContainer: Color(0xff000e0b),
      secondary: Color(0xfffff9f1),
      onSecondary: Color(0xff000000),
      secondaryContainer: Color(0xffe8dcc2),
      onSecondaryContainer: Color(0xff2c2615),
      tertiary: Color(0xffe4f6cf),
      onTertiary: Color(0xff000000),
      tertiaryContainer: Color(0xffb7c8a4),
      onTertiaryContainer: Color(0xff040f00),
      error: Color(0xffffece8),
      onError: Color(0xff000000),
      errorContainer: Color(0xffffaea0),
      onErrorContainer: Color(0xff130000),
      surface: Color(0xff141313),
      onSurface: Color(0xffffffff),
      onSurfaceVariant: Color(0xffffffff),
      outline: Color(0xfff3f0e6),
      outlineVariant: Color(0xffc5c2b9),
      shadow: Color(0xff000000),
      scrim: Color(0xff000000),
      inverseSurface: Color(0xffe5e2e1),
      inversePrimary: Color(0xff1e5048),
      primaryFixed: Color(0xffbaede2),
      onPrimaryFixed: Color(0xff000000),
      primaryFixedDim: Color(0xff9fd0c6),
      onPrimaryFixedVariant: Color(0xff001511),
      secondaryFixed: Color(0xffeee1c7),
      onSecondaryFixed: Color(0xff000000),
      secondaryFixedDim: Color(0xffd1c5ac),
      onSecondaryFixedVariant: Color(0xff161103),
      tertiaryFixed: Color(0xffd7e8c2),
      onTertiaryFixed: Color(0xff000000),
      tertiaryFixedDim: Color(0xffbbcca8),
      onTertiaryFixedVariant: Color(0xff081402),
      surfaceDim: Color(0xff141313),
      surfaceBright: Color(0xff51504f),
      surfaceContainerLowest: Color(0xff000000),
      surfaceContainerLow: Color(0xff201f1f),
      surfaceContainer: Color(0xff313030),
      surfaceContainerHigh: Color(0xff3c3b3b),
      surfaceContainerHighest: Color(0xff484646),
    );
  }

  ThemeData darkHighContrast() {
    return theme(darkHighContrastScheme());
  }

  ThemeData theme(ColorScheme colorScheme) => ThemeData(
    useMaterial3: true,
    brightness: colorScheme.brightness,
    colorScheme: colorScheme,
    textTheme: textTheme.apply(
      bodyColor: colorScheme.onSurface,
      displayColor: colorScheme.onSurface,
    ),
    scaffoldBackgroundColor: colorScheme.surface,
    canvasColor: colorScheme.surface,
  );

  List<ExtendedColor> get extendedColors => [];
}

class ExtendedColor {
  final Color seed, value;
  final ColorFamily light;
  final ColorFamily lightHighContrast;
  final ColorFamily lightMediumContrast;
  final ColorFamily dark;
  final ColorFamily darkHighContrast;
  final ColorFamily darkMediumContrast;

  const ExtendedColor({
    required this.seed,
    required this.value,
    required this.light,
    required this.lightHighContrast,
    required this.lightMediumContrast,
    required this.dark,
    required this.darkHighContrast,
    required this.darkMediumContrast,
  });
}

class ColorFamily {
  const ColorFamily({
    required this.color,
    required this.onColor,
    required this.colorContainer,
    required this.onColorContainer,
  });

  final Color color;
  final Color onColor;
  final Color colorContainer;
  final Color onColorContainer;
}
