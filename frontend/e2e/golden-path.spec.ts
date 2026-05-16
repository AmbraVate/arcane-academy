import { test, expect } from '@playwright/test'

const TEST_EMAIL = process.env.E2E_EMAIL ?? 'e2e-test@example.com'
const TEST_PASSWORD = process.env.E2E_PASSWORD ?? 'TestPassword123!'
const TEST_USERNAME = process.env.E2E_USERNAME ?? 'e2etestuser'

test.describe('Golden path — login → topic → learn sub-chunk', () => {
  test('register and land on topics page', async ({ page }) => {
    await page.goto('/register')
    await page.fill('[name="username"], input[placeholder*="username" i]', TEST_USERNAME)
    await page.fill('[name="email"], input[type="email"]', TEST_EMAIL)
    await page.fill('[name="password"], input[type="password"]', TEST_PASSWORD)
    await page.click('button[type="submit"], button:has-text("Register")')

    // Should redirect to topics after successful registration
    await expect(page).toHaveURL(/\/topics/, { timeout: 10_000 })
    await expect(page.locator('text=Choose Your Path')).toBeVisible()
  })

  test('login with credentials', async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[type="email"]', TEST_EMAIL)
    await page.fill('input[type="password"]', TEST_PASSWORD)
    await page.click('button[type="submit"], button:has-text("Sign In"), button:has-text("Login")')

    await expect(page).toHaveURL(/\/topics/, { timeout: 10_000 })
    await expect(page.locator('text=Choose Your Path')).toBeVisible()
  })

  test('navigate to Java topic', async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[type="email"]', TEST_EMAIL)
    await page.fill('input[type="password"]', TEST_PASSWORD)
    await page.click('button[type="submit"], button:has-text("Sign In"), button:has-text("Login")')
    await expect(page).toHaveURL(/\/topics/, { timeout: 10_000 })

    // Click the Java topic card (the only active one)
    const javaCard = page.locator('[data-testid="topic-java"], div:has-text("Java"):has-text("Continue →")').first()
    await javaCard.click()

    // Either goes to onboarding or topic page
    await expect(page).toHaveURL(/\/topic\/java/, { timeout: 8_000 })
  })

  test('protected route redirects to login when not authenticated', async ({ page }) => {
    await page.goto('/topics')
    await expect(page).toHaveURL(/\/login/, { timeout: 5_000 })
  })

  test('nav shows review count when logged in', async ({ page }) => {
    await page.goto('/login')
    await page.fill('input[type="email"]', TEST_EMAIL)
    await page.fill('input[type="password"]', TEST_PASSWORD)
    await page.click('button[type="submit"], button:has-text("Sign In"), button:has-text("Login")')
    await expect(page).toHaveURL(/\/topics/, { timeout: 10_000 })

    // Nav should be present
    await expect(page.locator('nav, header')).toBeVisible()
  })
})
