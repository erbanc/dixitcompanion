"""
Generates high-fidelity SVG mockups for Dixit Companion screens,
then converts them to PNG with cairosvg.
"""
import cairosvg, os, textwrap

OUT = "/home/user/dixitcompanion/mockups"
os.makedirs(OUT, exist_ok=True)

W, H = 360, 640

def svg_wrap(content, w=W, h=H):
    return f"""<?xml version="1.0" encoding="utf-8"?>
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
     width="{w}" height="{h}" viewBox="0 0 {w} {h}">
  <defs>
    <filter id="shadow" x="-20%" y="-20%" width="150%" height="150%">
      <feDropShadow dx="2" dy="3" stdDeviation="4" flood-color="#00000028"/>
    </filter>
    <filter id="shadow-sm" x="-20%" y="-20%" width="150%" height="150%">
      <feDropShadow dx="1" dy="2" stdDeviation="2" flood-color="#00000020"/>
    </filter>
    <filter id="glow-gold">
      <feDropShadow dx="0" dy="0" stdDeviation="6" flood-color="#ECA52E88"/>
    </filter>
    <linearGradient id="grad-home" x1="0" y1="0" x2="0" y2="1">
      <stop offset="0%"   stop-color="#2A1660"/>
      <stop offset="50%"  stop-color="#5C3A9E"/>
      <stop offset="100%" stop-color="#7152BA"/>
    </linearGradient>
    <linearGradient id="grad-btn-gold" x1="0" y1="0" x2="0" y2="1">
      <stop offset="0%"  stop-color="#F4B83A"/>
      <stop offset="100%" stop-color="#E09820"/>
    </linearGradient>
    <linearGradient id="grad-btn-lav" x1="0" y1="0" x2="0" y2="1">
      <stop offset="0%"  stop-color="#B89EDE"/>
      <stop offset="100%" stop-color="#9A78C8"/>
    </linearGradient>
    <linearGradient id="grad-btn-coral" x1="0" y1="0" x2="0" y2="1">
      <stop offset="0%"  stop-color="#ED8270"/>
      <stop offset="100%" stop-color="#D45F48"/>
    </linearGradient>
    <linearGradient id="grad-btn-mint" x1="0" y1="0" x2="0" y2="1">
      <stop offset="0%"  stop-color="#6ECEC7"/>
      <stop offset="100%" stop-color="#48ADA6"/>
    </linearGradient>
    <linearGradient id="grad-btn-purple" x1="0" y1="0" x2="0" y2="1">
      <stop offset="0%"  stop-color="#6B45AC"/>
      <stop offset="100%" stop-color="#4A2E80"/>
    </linearGradient>
  </defs>
{content}
</svg>"""

def card(x, y, w, h, r=16, shadow=True):
    s = f' filter="url(#shadow)"' if shadow else ''
    return f'<rect x="{x}" y="{y}" width="{w}" height="{h}" rx="{r}"{s} fill="white"/>'

def pill_btn(x, y, w, h, grad, label, font_size=17, text_color="white", shadow_id="shadow-sm"):
    r = h // 2
    cx = x + w // 2; cy = y + h // 2
    return f"""
  <rect x="{x}" y="{y}" width="{w}" height="{h}" rx="{r}" filter="url(#{shadow_id})" fill="url(#{grad})"/>
  <text x="{cx}" y="{cy+1}" text-anchor="middle" dominant-baseline="middle"
        font-family="Georgia, serif" font-size="{font_size}" font-weight="bold" fill="{text_color}">{label}</text>"""

def badge(x, y, text, font_size=11):
    return f"""
  <rect x="{x}" y="{y}" width="72" height="24" rx="12" fill="#EDE0FF"/>
  <text x="{x+36}" y="{y+12}" text-anchor="middle" dominant-baseline="middle"
        font-family="Arial, sans-serif" font-size="{font_size}" font-weight="bold" fill="#5C3A9E">{text}</text>"""

def divider(x1, y, x2):
    return f'<line x1="{x1}" y1="{y}" x2="{x2}" y2="{y}" stroke="#EDE0FF" stroke-width="1"/>'

def stars_svg(n=30, seed=42):
    import random; random.seed(seed)
    out = []
    for _ in range(n):
        sx = random.randint(0, W); sy = random.randint(0, H // 2)
        r = random.uniform(0.8, 2.5); op = random.uniform(0.4, 0.9)
        out.append(f'<circle cx="{sx}" cy="{sy}" r="{r}" fill="#FFFDE0" opacity="{op:.2f}"/>')
    return "\n".join(out)


# ════════════════════════════════════════════════════════════════
# 1. HOME SCREEN
# ════════════════════════════════════════════════════════════════
content = f"""
  <rect width="{W}" height="{H}" fill="url(#grad-home)"/>
  {stars_svg()}
  <!-- decorative circles -->
  <circle cx="300" cy="80" r="60" fill="#7B52C0" opacity="0.3"/>
  <circle cx="30"  cy="200" r="40" fill="#A98DD4" opacity="0.2"/>
  <circle cx="180" cy="580" r="80" fill="#3A2275" opacity="0.4"/>

  <!-- title shadow -->
  <text x="182" y="162" text-anchor="middle"
        font-family="Georgia, 'Times New Roman', serif" font-size="72"
        font-weight="bold" fill="#00000040">Dixit</text>
  <!-- title -->
  <text x="180" y="160" text-anchor="middle" filter="url(#glow-gold)"
        font-family="Georgia, 'Times New Roman', serif" font-size="72"
        font-weight="bold" fill="#ECA52E">Dixit</text>
  <!-- subtitle -->
  <text x="180" y="192" text-anchor="middle"
        font-family="Arial, sans-serif" font-size="15" letter-spacing="4"
        fill="#D4C0FF" opacity="0.9">C O M P A N I O N</text>

  <!-- decorative line -->
  <line x1="100" y1="205" x2="260" y2="205" stroke="#A98DD4" stroke-width="1" opacity="0.6"/>

  {pill_btn(32, 240, 296, 56, "grad-btn-gold",    "✦  Nouvelle partie",  18, "#1E1133")}
  {pill_btn(32, 312, 296, 56, "grad-btn-lav",     "♟  Statistiques",     18)}
  {pill_btn(32, 384, 296, 56, "grad-btn-coral",   "📖  Règles du jeu",   18)}

  <!-- version hint -->
  <text x="180" y="490" text-anchor="middle" font-family="Arial" font-size="11"
        fill="#FFFFFF40">v1.0</text>
"""
svg = svg_wrap(content)
cairosvg.svg2png(bytestring=svg.encode(), write_to=f"{OUT}/01_homescreen.png", scale=2)
print("1 ✓")


# ════════════════════════════════════════════════════════════════
# 2. SELECT PLAYERS
# ════════════════════════════════════════════════════════════════
chips = [("Alice","#A98DD4"),("Bob","#5DBFB8"),("Charlie","#E5735D"),("Diana","#ECA52E"),("Erwan","#5C3A9E")]
chip_svg = ""
cx2, cy2 = 40, 210
for name, col in chips:
    cw = len(name) * 9 + 36
    chip_svg += f"""
  <rect x="{cx2}" y="{cy2}" width="{cw}" height="30" rx="15" fill="{col}"/>
  <text x="{cx2+12}" y="{cy2+15}" dominant-baseline="middle"
        font-family="Arial" font-size="13" fill="white">{name}</text>
  <text x="{cx2+cw-14}" y="{cy2+15}" dominant-baseline="middle"
        font-family="Arial" font-size="13" fill="white" opacity="0.8">×</text>"""
    cx2 += cw + 8
    if cx2 > W - 80: cx2 = 40; cy2 += 40

content = f"""
  <rect width="{W}" height="{H}" fill="#FEF6EB"/>

  <!-- title -->
  <text x="180" y="72" text-anchor="middle"
        font-family="Georgia,serif" font-size="42" font-weight="bold" fill="#1E1133">Joueurs</text>

  <!-- input card -->
  {card(24, 102, W-48, 72, 14)}
  <text x="48" y="130" font-family="Arial" font-size="12" fill="#A98DD4">Nom du joueur</text>
  <line x1="48" y1="155" x2="{W-80}" y2="155" stroke="#A98DD4" stroke-width="1.5"/>
  <!-- add button -->
  <rect x="{W-64}" y="112" width="40" height="40" rx="20" fill="#5C3A9E" filter="url(#shadow-sm)"/>
  <text x="{W-44}" y="132" text-anchor="middle" dominant-baseline="middle"
        font-family="Arial" font-size="22" font-weight="bold" fill="white">+</text>

  <!-- chips label -->
  <text x="40" y="200" font-family="Arial" font-size="12" fill="#A98DD4">5 joueurs ajoutés</text>
  {chip_svg}

  <!-- info hint -->
  <rect x="24" y="310" width="{W-48}" height="44" rx="10" fill="#EDE0FF" opacity="0.6"/>
  <text x="180" y="332" text-anchor="middle" dominant-baseline="middle"
        font-family="Arial" font-size="12" fill="#5C3A9E">Minimum 3 joueurs requis</text>

  <!-- CTA bar -->
  <rect x="0" y="{H-72}" width="{W}" height="72" fill="url(#grad-btn-purple)"/>
  <text x="180" y="{H-36}" text-anchor="middle" dominant-baseline="middle"
        font-family="Georgia,serif" font-size="22" fill="white">Continuer →</text>
"""
svg = svg_wrap(content)
cairosvg.svg2png(bytestring=svg.encode(), write_to=f"{OUT}/02_select_players.png", scale=2)
print("2 ✓")


# ════════════════════════════════════════════════════════════════
# 3. EVERYONE FOUND?
# ════════════════════════════════════════════════════════════════
content = f"""
  <rect width="{W}" height="{H}" fill="#FEF6EB"/>
  {badge(16, 14, "TOUR 3")}

  <text x="180" y="100" text-anchor="middle"
        font-family="Georgia,serif" font-size="34" font-weight="bold" fill="#1E1133">Tout le monde</text>
  <text x="180" y="140" text-anchor="middle"
        font-family="Georgia,serif" font-size="34" font-weight="bold" fill="#1E1133">a trouvé ?</text>
  <text x="180" y="175" text-anchor="middle"
        font-family="Arial" font-size="13" fill="#A98DD4">La carte du conteur</text>

  <!-- OUI button -->
  <rect x="16" y="205" width="{W//2-22}" height="{H-237}" rx="20"
        fill="url(#grad-btn-mint)" filter="url(#shadow)"/>
  <text x="{(16 + W//2-22)//2}" y="440" text-anchor="middle"
        font-family="Georgia,serif" font-size="48" font-weight="bold" fill="white">OUI</text>
  <text x="{(16 + W//2-22)//2}" y="490" text-anchor="middle"
        font-family="Arial" font-size="13" fill="white" opacity="0.8">Tout le monde</text>

  <!-- NON button -->
  <rect x="{W//2+6}" y="205" width="{W//2-22}" height="{H-237}" rx="20"
        fill="url(#grad-btn-coral)" filter="url(#shadow)"/>
  <text x="{W//2+6 + (W//2-22)//2}" y="440" text-anchor="middle"
        font-family="Georgia,serif" font-size="48" font-weight="bold" fill="white">NON</text>
  <text x="{W//2+6 + (W//2-22)//2}" y="490" text-anchor="middle"
        font-family="Arial" font-size="13" fill="white" opacity="0.8">Certains ont raté</text>
"""
svg = svg_wrap(content)
cairosvg.svg2png(bytestring=svg.encode(), write_to=f"{OUT}/03_everyone_found.png", scale=2)
print("3 ✓")


# ════════════════════════════════════════════════════════════════
# 4. END TURN — scores
# ════════════════════════════════════════════════════════════════
players_scores = [
    ("Alice", "12 pts", True,  "+3"),
    ("Bob",   "9 pts",  False, "+1"),
    ("Charlie","8 pts", False, "+2"),
    ("Diana", "7 pts",  False, "0"),
]
rows_svg = ""
ry = 152
for i, (nm, sc, win, delta) in enumerate(players_scores):
    row_fill = "#F5EEFF" if win else "white"
    rows_svg += f'<rect x="36" y="{ry}" width="{W-72}" height="58" rx="12" fill="{row_fill}"/>'
    if win:
        rows_svg += f'<text x="54" y="{ry+30}" dominant-baseline="middle" font-family="Arial" font-size="18" fill="#ECA52E">★</text>'
    rows_svg += f'<text x="{"80" if win else "60"}" y="{ry+30}" dominant-baseline="middle" font-family="Georgia,serif" font-size="19" fill="#1E1133">{nm}</text>'
    rows_svg += f'<text x="{W-52}" y="{ry+22}" text-anchor="end" dominant-baseline="middle" font-family="Georgia,serif" font-size="19" font-weight="bold" fill="#5C3A9E">{sc}</text>'
    delta_col = "#5DBFB8" if "+" in delta else ("#E5735D" if delta == "0" else "#5DBFB8")
    rows_svg += f'<text x="{W-52}" y="{ry+44}" text-anchor="end" font-family="Arial" font-size="12" fill="{delta_col}">{delta} ce tour</text>'
    if i < len(players_scores)-1:
        rows_svg += divider(52, ry+60, W-52)
    ry += 68

content = f"""
  <rect width="{W}" height="{H}" fill="#FEF6EB"/>
  {badge(16, 14, "TOUR 3")}

  <text x="180" y="86" text-anchor="middle"
        font-family="Georgia,serif" font-size="36" font-weight="bold" fill="#1E1133">Fin du tour ✦</text>

  <!-- scores card -->
  {card(24, 128, W-48, H-210, 16)}
  <text x="48" y="154" font-family="Arial" font-size="11" fill="#A98DD4" letter-spacing="1">CLASSEMENT</text>

  {rows_svg}

  <!-- CTA -->
  <rect x="0" y="{H-72}" width="{W}" height="72" fill="url(#grad-btn-purple)"/>
  <text x="180" y="{H-36}" text-anchor="middle" dominant-baseline="middle"
        font-family="Georgia,serif" font-size="22" fill="white">Tour suivant →</text>
"""
svg = svg_wrap(content)
cairosvg.svg2png(bytestring=svg.encode(), write_to=f"{OUT}/04_end_turn.png", scale=2)
print("4 ✓")


# ════════════════════════════════════════════════════════════════
# 5. STATS
# ════════════════════════════════════════════════════════════════
stat_rows = [("Alice","12","8","67%"),("Bob","12","2","17%"),("Charlie","10","1","10%"),("Diana","8","1","12%")]
hdrs = ["Joueur","Parties","Victoires","%"]
cw_stat = (W-32) // 4
table_svg = ""
# header
for i, h in enumerate(hdrs):
    table_svg += f'<text x="{16 + cw_stat*i + cw_stat//2}" y="138" text-anchor="middle" dominant-baseline="middle" font-family="Arial" font-size="12" font-weight="bold" fill="white">{h}</text>'
# rows
for ri, (nm,ng,nw3,pc) in enumerate(stat_rows):
    ry2 = 160 + ri*44
    row_fill = "#F5EEFF" if ri == 0 else ("white" if ri%2==0 else "#FDFAFF")
    table_svg += f'<rect x="16" y="{ry2}" width="{W-32}" height="44" fill="{row_fill}"/>'
    for ci, cell in enumerate([nm,ng,nw3,pc]):
        col = "#5C3A9E" if ci==0 else "#1E1133"
        fw = "bold" if ci==0 else "normal"
        table_svg += f'<text x="{16+cw_stat*ci+cw_stat//2}" y="{ry2+22}" text-anchor="middle" dominant-baseline="middle" font-family="Arial" font-size="13" font-weight="{fw}" fill="{col}">{cell}</text>'
    table_svg += divider(24, ry2+44, W-24)

content = f"""
  <rect width="{W}" height="{H}" fill="#FEF6EB"/>
  <text x="180" y="68" text-anchor="middle"
        font-family="Georgia,serif" font-size="38" font-weight="bold" fill="#1E1133">Statistiques</text>

  {card(16, 102, W-32, H-118, 16)}
  <!-- purple header -->
  <rect x="16" y="102" width="{W-32}" height="48" rx="16" fill="#5C3A9E"/>
  <rect x="16" y="126" width="{W-32}" height="24" fill="#5C3A9E"/>
  {table_svg}
"""
svg = svg_wrap(content)
cairosvg.svg2png(bytestring=svg.encode(), write_to=f"{OUT}/05_stats.png", scale=2)
print("5 ✓")


# ════════════════════════════════════════════════════════════════
# 6. SELECT STORYTELLER
# ════════════════════════════════════════════════════════════════
storytellers = ["Alice","Bob","Charlie","Diana","Erwan"]
radio_svg = ""
ry6 = 150
for i, nm in enumerate(storytellers):
    row_fill = "#F0E6FF" if i==0 else "white"
    radio_svg += f'<rect x="36" y="{ry6}" width="{W-72}" height="54" rx="12" fill="{row_fill}"/>'
    # radio
    radio_svg += f'<circle cx="62" cy="{ry6+27}" r="10" stroke="#5C3A9E" stroke-width="2" fill="white"/>'
    if i == 0:
        radio_svg += f'<circle cx="62" cy="{ry6+27}" r="5" fill="#5C3A9E"/>'
    radio_svg += f'<text x="84" y="{ry6+27}" dominant-baseline="middle" font-family="Georgia,serif" font-size="19" fill="#1E1133">{nm}</text>'
    if i == 0:
        radio_svg += f'<rect x="{W-90}" y="{ry6+14}" width="48" height="26" rx="13" fill="#EDE0FF"/>'
        radio_svg += f'<text x="{W-66}" y="{ry6+27}" text-anchor="middle" dominant-baseline="middle" font-family="Arial" font-size="11" fill="#5C3A9E">Tour !</text>'
    if i < len(storytellers)-1:
        radio_svg += divider(52, ry6+56, W-52)
    ry6 += 62

content = f"""
  <rect width="{W}" height="{H}" fill="#FEF6EB"/>
  {badge(16, 14, "TOUR 4")}

  <text x="180" y="86" text-anchor="middle"
        font-family="Georgia,serif" font-size="36" font-weight="bold" fill="#1E1133">Qui raconte ?</text>
  <text x="180" y="118" text-anchor="middle"
        font-family="Arial" font-size="13" fill="#A98DD4">Sélectionne le conteur du tour</text>

  {card(24, 138, W-48, H-222, 16)}
  {radio_svg}

  <rect x="0" y="{H-72}" width="{W}" height="72" fill="url(#grad-btn-purple)"/>
  <text x="180" y="{H-36}" text-anchor="middle" dominant-baseline="middle"
        font-family="Georgia,serif" font-size="22" fill="white">C'est parti ! ✦</text>
"""
svg = svg_wrap(content)
cairosvg.svg2png(bytestring=svg.encode(), write_to=f"{OUT}/06_select_storyteller.png", scale=2)
print("6 ✓")

print(f"\nAll done → {OUT}")
