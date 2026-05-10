// ESLint config — minimal but real.
//
// Goal: catch genuine bugs (unused vars in production code, undeclared
// references, accidental "any" cast holes) WITHOUT noisy stylistic rules
// that would flag a thousand existing lines on first run. We can tighten
// incrementally — start by promoting warnings to errors as the codebase
// settles.
//
// `npm run lint` was wired in package.json long before any config existed.
// This file is the missing piece; CI now executes a real check.
module.exports = {
  root: true,
  env: { browser: true, es2020: true, node: true },
  extends: [
    'eslint:recommended',
    'plugin:@typescript-eslint/recommended',
  ],
  ignorePatterns: [
    'dist',
    'build',
    'node_modules',
    '.eslintrc.cjs',
    'vite.config.ts',
    'vitest.config.ts',
  ],
  parser: '@typescript-eslint/parser',
  parserOptions: {
    ecmaVersion: 2022,
    sourceType: 'module',
    ecmaFeatures: { jsx: true },
  },
  plugins: ['@typescript-eslint'],
  rules: {
    // Existing code uses `any` in several DTO seams (Map<String, Object> from
    // Jackson, content-system raw JSON). Keep as warning while we annotate.
    '@typescript-eslint/no-explicit-any': 'warn',

    // Unused-vars: prefix with `_` to silence (standard convention).
    '@typescript-eslint/no-unused-vars': ['warn', {
      argsIgnorePattern: '^_',
      varsIgnorePattern: '^_',
      caughtErrorsIgnorePattern: '^_',
    }],
    // The base rule and the TS rule overlap — disable the base.
    'no-unused-vars': 'off',

    // The codebase intentionally uses `@ts-ignore` in a few legitimate spots
    // (e.g. third-party type gaps). Allow with explanation comments later.
    '@typescript-eslint/ban-ts-comment': ['warn', {
      'ts-ignore': 'allow-with-description',
      'ts-expect-error': 'allow-with-description',
    }],
  },
  overrides: [
    {
      // Test files often use `any` casts and unused setup vars freely.
      files: ['**/*.test.ts', '**/*.test.tsx', '**/*.spec.ts', '**/*.spec.tsx', 'src/test/**', 'src/__tests__/**'],
      rules: {
        '@typescript-eslint/no-explicit-any': 'off',
        '@typescript-eslint/no-unused-vars': 'off',
      },
    },
  ],
}
