import sys, os
import io
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

base = r'D:\Coding\Real_Projects\arcane-academy\arcane-academy\frontend\src'

# Check specific files for remaining issues
check_files = [
  r'features\learning\pages\EncodingPage.tsx',
  r'features\onboarding\pages\PrerequisiteCheckPage.tsx',
  r'features\leaderboard\pages\LeaderboardPage.tsx',
  r'features\admin\pages\AdminSubChunksPage.tsx',
]

bad_patterns = ['â€', 'â†', 'âœ', 'â˜', 'âš', 'â"', 'ðŸ', 'Ã°', 'Ã…', 'Ã¢']

for f in check_files:
    full_path = os.path.join(base, f)
    print("=== " + f + " ===")
    with open(full_path, 'r', encoding='utf-8') as fh:
        content = fh.read()
    for pat in bad_patterns:
        start = 0
        count = 0
        while True:
            idx = content.find(pat, start)
            if idx < 0 or count >= 3:
                break
            ctx = content[max(0,idx-15):idx+25]
            codepoints = [hex(ord(c)) for c in content[idx:idx+len(pat)+4]]
            print("  " + repr(pat) + " at " + str(idx) + ": " + repr(ctx))
            print("    codepoints: " + str(codepoints))
            start = idx + 1
            count += 1
    print("")
