import sys, os
import io
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

base = r'D:\Coding\Real_Projects\arcane-academy\arcane-academy\frontend\src'

files = [
  'App.tsx',
  r'features\home\pages\HomePage.tsx',
  r'features\learning\pages\EncodingPage.tsx',
  r'features\profile\pages\ProfilePage.tsx',
  r'features\onboarding\pages\CssPrimerPage.tsx',
  r'features\onboarding\pages\PrerequisiteCheckPage.tsx',
  r'features\exploration\pages\CuriosityQueuePage.tsx',
  r'components\StuckButton.tsx',
  r'features\onboarding\pages\DomainOnboardingPage.tsx',
  r'features\diagnostic\pages\DomainDiagnosticPage.tsx',
  r'features\admin\pages\AdminDomainsPage.tsx',
  r'features\admin\pages\AdminImportExportPage.tsx',
  r'features\admin\pages\AdminChunksPage.tsx',
  r'features\profile\pages\PublicProfilePage.tsx',
  r'features\onboarding\data\cssPrimer.ts',
  r'features\learning\components\StoryPanel.tsx',
  r'features\learning\components\RabbitHoleHtml.tsx',
  r'features\leaderboard\pages\LeaderboardPage.tsx',
  r'features\admin\pages\AdminSubChunksPage.tsx',
  r'features\admin\pages\AdminSubChunkEditorPage.tsx',
  r'features\admin\pages\AdminQuestionsPage.tsx',
  r'features\admin\pages\AdminCapstonesPage.tsx',
  r'features\domains\pages\ModuleMapPage.tsx',
]

# Check for remaining known-bad mojibake patterns
bad_patterns = ['â€', 'Â·', 'Â£', 'â†', 'âœ', 'â˜', 'âš', 'â"', 'ðŸ', 'Ã°', 'Ã…', 'Ã¢']

issues_found = 0
for f in files:
    full_path = os.path.join(base, f)
    if not os.path.exists(full_path):
        print("NOT FOUND: " + f)
        continue
    with open(full_path, 'r', encoding='utf-8') as fh:
        content = fh.read()
    file_issues = []
    for pat in bad_patterns:
        if pat in content:
            idx = content.find(pat)
            ctx = content[max(0,idx-10):idx+20]
            file_issues.append(pat)
    if file_issues:
        print("REMAINING ISSUES in " + f + ": " + str(file_issues))
        issues_found += len(file_issues)
    else:
        print("Clean: " + f)

print("")
print("Total remaining issue count: " + str(issues_found))
