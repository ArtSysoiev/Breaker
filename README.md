# Breaker

A logic puzzle game inspired by Wordle, but using digits instead of letters. The player's goal is to crack the password (sequence of digits) within 5 attempts.

### Setup Screen
<img width="304" height="675" alt="image" src="https://github.com/user-attachments/assets/9b4dc816-b7df-4fee-b1bb-545df1df749a" /><img width="304" height="676" alt="image" src="https://github.com/user-attachments/assets/d4ad0bcf-23ff-4391-99e7-e9c88dcb165f" /><img width="304" height="675" alt="image" src="https://github.com/user-attachments/assets/d8d483ee-aefc-45b0-a7b8-f2b867187232" />

Configure your gaming experience with personalized settings:
- Enter your username
- Choose light or dark theme
- Select interface language: English, Ukrainian, Russian, or Polish

### Gameplay
After each attempt, you receive feedback using colored indicators:
- 🟩 **Green** - Digit is in the correct position
- 🟨 **Yellow** - Digit exists in the password but in wrong position  
- ⬜ **Gray** - Digit doesn't exist in the password

### Game Flow
- Start with 5 empty input fields
- First field automatically gets focus and keyboard opens
<img width="310" height="690" alt="image" src="https://github.com/user-attachments/assets/69c46a22-be2d-4374-9020-d3342f4456a5" />

- After entering a digit, focus moves to the next field

<img width="278" height="618" alt="image" src="https://github.com/user-attachments/assets/9bdc1249-9654-4d28-b468-29e164490d6c" /><img width="278" height="618" alt="image" src="https://github.com/user-attachments/assets/22f1da3f-271d-4c63-ab1a-b68716f5de71" />

- When all fields are filled, keyboard closes and Continue button becomes available
<img width="310" height="689" alt="image" src="https://github.com/user-attachments/assets/93279f51-d239-4eea-bafe-f2b07957f03d" />

- Press Continue to see your attempt results with color feedback
<img width="329" height="731" alt="image" src="https://github.com/user-attachments/assets/fc9c6406-645a-441c-a917-569d879560c2" />

- Restart button available in top-right corner during gameplay

## ✨ Features

### 🎮 Core Gameplay
- **5 attempts** to guess the correct digit sequence
<img width="335" height="744" alt="image" src="https://github.com/user-attachments/assets/3fa2eb18-9963-476c-b70b-8b661c1f4ca7" />

- **Immediate feedback** with color-coded hints
- **Smooth animations** for enhanced user experience
- **Input validation** with helpful error messages
<img width="331" height="577" alt="image" src="https://github.com/user-attachments/assets/cd3dbb0d-10a9-4bb7-b834-91773556de1c" />

- **Auto-focus** progression through input fields

### ⚙️ Settings & Personalization
- **Multi-language support**: English, Ukrainian, Russian, Polish
- **Theme options**: Light and dark modes
- **Persistent settings** using SharedPreferences
- **Custom username** configuration

### 📊 Game Statistics
<img width="344" height="765" alt="image" src="https://github.com/user-attachments/assets/6bbba8fc-1d1b-4f89-876c-566162f3a34f" />

- **Game history** stored in local database
- **Detailed statistics** including:
  - Game number
  - Date and time
  - Number of attempts used
  - Result (win/loss)

### 🎯 Navigation & UI
<img width="379" height="842" alt="image" src="https://github.com/user-attachments/assets/1034035a-d2d9-42f9-9029-f94a1f5fa30d" />

- **Navigation Drawer** with user stats:
  - Username display
  - Total games played
  - Total wins
  - Current win streak
- **History screen** with RecyclerView table format
- **Settings access** from navigation menu
- **Clean, intuitive interface** with CardView components
---
