import os
import sys

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

# All characters that may have been double-encoded
# They were: originally UTF-8 bytes -> read as cp1252 -> saved as UTF-8 again
# Fix by: taking the cp1252-decoded string, re-encoding as cp1252 to get original bytes, then decode as UTF-8
correct_chars = [
    '—',  # em dash —
    '’',  # right single quote '
    '‘',  # left single quote '
    '·',  # middle dot ·
    '£',  # pound sign £
    '→',  # right arrow →
    '←',  # left arrow ←
    '✦',  # ✦
    '☕',  # ☕
    '\U0001F525',  # 🔥
    '\U0001F6AA',  # 🚪
    '\U0001F3A8',  # 🎨
    '⚛',  # ⚛
    '\U0001F5C3',  # 🗃
    '\U0001F9E0',  # 🧠
    '\U0001F333',  # 🌳
    '\U0001F52C',  # 🔬
    '\U0001F512',  # 🔒
    '\U0001F516',  # 📖
    '─',  # ─ box drawing horizontal
    '…',  # … ellipsis
    '✓',  # ✓ check mark
    ' ',  # non-breaking space
    '°',  # degree sign °
    '•',  # bullet •
    '–',  # en dash –
    '“',  # left double quote "
    '”',  # right double quote "
    '\U0001F4CC',  # 📌
    '\U0001F4A1',  # 💡
    '\U0001F6A7',  # 🚧
    '←',  # ← left arrow
    '↑',  # ↑ up arrow
    '↓',  # ↓ down arrow
    '✔',  # ✔ heavy check mark
    '✨',  # ✨
    '❤',  # ❤
    '♥',  # ♥
    '★',  # ★
    '\U0001F4D6',  # 📖
    '\U0001F31F',  # 🌟
    '\U0001F3AF',  # 🎯
    '\U0001F4CA',  # 📊
    '\U0001F527',  # 🔧
    '\U0001F4DD',  # 📝
    '\U0001F680',  # 🚀
    '\U0001F4A5',  # 💥
    '\U0001F4AF',  # 💯
    '\U0001F535',  # 🔵
    '\U0001F534',  # 🔴
    '\U0001F7E2',  # 🟢
    '\U0001F4A4',  # 💤
    '\U0001F4A8',  # 💨
    '\U0001F3C6',  # 🏆
    '\U0001F947',  # 🥇
    '\U0001F4A3',  # 💣
    '\U0001F4B0',  # 💰
    '⚠',  # ⚠
    '✔',  # ✔
    '❌',  # ❌
    '✅',  # ✅
    '\U0001F504',  # 🔄
    '\U0001F4E4',  # 📤
    '\U0001F4E5',  # 📥
    '\U0001F4CB',  # 📋
    '\U0001F4C2',  # 📂
    '\U0001F5C2',  # 🗂
    '\U0001F914',  # 🤔
    '\U0001F44D',  # 👍
    '\U0001F44E',  # 👎
    '\U0001F440',  # 👀
    '\U0001F6AB',  # 🚫
    '\U0001F50D',  # 🔍
    '\U0001F4AC',  # 💬
    '\U0001F4A7',  # 💧
    '\U0001F30A',  # 🌊
    '\U0001F6E1',  # 🛡
    '\U0001F9EA',  # 🧪
    '\U0001F9F0',  # 🧰
    '\U0001F4FC',  # 📼
    '\U0001F4FB',  # 📻
    '\U0001F4FA',  # 📺
    '\U0001F4F7',  # 📷
    '\U0001F30D',  # 🌍
    '\U0001F30E',  # 🌎
    '\U0001F30F',  # 🌏
    '\U0001F319',  # 🌙
    '\U0001F4BB',  # 💻
    '\U0001F4F1',  # 📱
    '☁',  # ☁
    '\U0001F004',  # 🀄
    '\U0001F3B2',  # 🎲
    '\U0001F9E9',  # 🧩
    '\U0001F4CD',  # 📍
    '\U0001F4CE',  # 📎
    '\U0001F517',  # 🔗
    '\U0001F510',  # 🔐
    '\U0001F513',  # 🔓
    '\U0001F511',  # 🔑
    '\U0001F5DD',  # 🗝
    '\U0001F6AA',  # 🚪
    '\U0001F3E0',  # 🏠
    '\U0001F3E1',  # 🏡
    '\U0001F3EB',  # 🏫
    '\U0001F3D7',  # 🏗
    '\U0001F6A6',  # 🚦
    '\U0001F6A5',  # 🚥
    '\U0001F3C3',  # 🏃
    '\U0001F468',  # 👨
    '\U0001F469',  # 👩
    '\U0001F9D1',  # 🧑
    '\U0001F464',  # 👤
    '\U0001F465',  # 👥
    '\U0001F4AA',  # 💪
    '\U0001F9E0',  # 🧠 (already listed)
    '\U0001F4D4',  # 📔
    '\U0001F4D5',  # 📕
    '\U0001F4D7',  # 📗
    '\U0001F4D8',  # 📘
    '\U0001F4D9',  # 📙
    '\U0001F4DA',  # 📚
    '\U0001F4DC',  # 📜
    '\U0001F4C3',  # 📃
    '\U0001F4C4',  # 📄
    '\U0001F4C5',  # 📅
    '\U0001F4C6',  # 📆
    '\U0001F4C7',  # 📇
    '\U0001F4C8',  # 📈
    '\U0001F4C9',  # 📉
    '\U0001F4F0',  # 📰
    '\U0001F4EF',  # 📯
    '\U0001F4EE',  # 📮
    '\U0001F4ED',  # 📭
    '\U0001F4EC',  # 📬
    '\U0001F4EB',  # 📫
    '\U0001F4EA',  # 📪
    '\U0001F4E9',  # 📩
    '\U0001F4E8',  # 📨
    '\U0001F4E7',  # 📧
    '\U0001F4E6',  # 📦
    '\U0001F4E3',  # 📣
    '\U0001F4E2',  # 📢
    '\U0001F4E1',  # 📡
    '\U0001F4E0',  # 📠
    '\U0001F4DF',  # 📟
    '\U0001F4DE',  # 📞
    '\U0001F4DB',  # 📛
    '\U0001F4D3',  # 📓
    '\U0001F4D2',  # 📒
    '\U0001F4D1',  # 📑
    '\U0001F4D0',  # 📐
    '\U0001F4CF',  # 📏
    '\U0001F4BE',  # 💾
    '\U0001F4BF',  # 💿
    '\U0001F4C0',  # 📀
    '\U0001F4BD',  # 💽
    '\U0001F4BC',  # 💼
    '\U0001F4B9',  # 💹
    '\U0001F4B8',  # 💸
    '\U0001F4B7',  # 💷
    '\U0001F4B6',  # 💶
    '\U0001F4B5',  # 💵
    '\U0001F4B4',  # 💴
    '\U0001F4B3',  # 💳
    '\U0001F4B2',  # 💲
    '\U0001F4B1',  # 💱
]

def build_replacements():
    replacements = []
    seen_mojibake = set()
    for ch in correct_chars:
        try:
            # Encode to UTF-8 bytes, decode as cp1252 to get the mojibake string
            utf8_bytes = ch.encode('utf-8')
            mojibake = utf8_bytes.decode('cp1252')
            if mojibake != ch and mojibake not in seen_mojibake:
                replacements.append((mojibake, ch))
                seen_mojibake.add(mojibake)
        except (UnicodeDecodeError, UnicodeEncodeError):
            pass
    # Sort by length descending to avoid partial replacements
    replacements.sort(key=lambda x: len(x[0]), reverse=True)
    return replacements

replacements = build_replacements()

import io
_stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
print(f'Built {len(replacements)} replacement patterns')

fixed_count = 0
for f in files:
    full_path = os.path.join(base, f)
    if not os.path.exists(full_path):
        print(f'NOT FOUND: {f}')
        continue
    with open(full_path, 'r', encoding='utf-8') as fh:
        content = fh.read()
    original = content
    for bad, good in replacements:
        content = content.replace(bad, good)
    if content != original:
        with open(full_path, 'w', encoding='utf-8', newline='') as fh:
            fh.write(content)
        # Count changes
        changes = sum(1 for a, b in zip(original, content) if a != b)
        print(f'Fixed: {f}')
        fixed_count += 1
    else:
        print(f'No changes: {f}')

print(f'\nTotal files fixed: {fixed_count}')
