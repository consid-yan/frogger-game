# 🐸 Frogger Game - COMP2011J Project

![Java](https://img.shields.io/badge/Java-JDK%2025-red.svg)
![IntelliJ](https://img.shields.io/badge/IDE-IntelliJ%20IDEA-green.svg)
![Course](https://img.shields.io/badge/Course-COMP2011J-orange.svg)

## 📖 Table of Contents
- [About](#about)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Game Controls](#game-controls)
- [Acknowledgments](#acknowledgments)

## 🎮 About

A classic **Frogger** arcade game implementation built with **JavaFX** for the COMP2011J course project. Guide the frog across busy roads and treacherous rivers to reach the safety of the lily pads!

**Key Features:**
- Smooth animations and responsive controls
- Multiple levels with increasing difficulty
- Score tracking and lives system

---

## ⚙️ Prerequisites

Before opening this project, ensure you have the following installed:

- **JDK 25** - Download from [Oracle JDK 25](https://www.oracle.com/java/technologies/downloads/) or use OpenJDK 25
- **IntelliJ IDEA** (2023.3 or newer recommended) - [Download here](https://www.jetbrains.com/idea/download/)
- **JavaFX SDK 21+** - [Download from Gluon](https://gluonhq.com/products/javafx/)

> **⚠️ Important**: JDK 25 **must** be installed **before** opening the project in IntelliJ to ensure proper module system compatibility.

---

## 🛠️ Installation & Setup

### Step 1: Configure IntelliJ
1. Open IntelliJ IDEA
2. **File → Open...** and select the **`Frogger` project folder** 
3. When prompted, **Trust the project**
4. Wait for IntelliJ to import Gradle/Maven dependencies (if applicable)

> **💡 Opening Clarification for Testers & Players**:  
> Ensure you open the **`Frogger` folder itself** as the IntelliJ project root. This is the folder that directly contains the `src` folder and project files.  
> - ✅ **Correct**: Select the inner `Frogger` folder  
> - ❌ **Incorrect**: Do not open a parent folder, or individual `.java` files

### Step 2: Verify JDK
Ensure Project SDK is set to JDK 25:
- **File → Project Structure → Project SDK → 25**

---

## 🎮 Game Controls

| Key | Action            |
|-----|-------------------|
| `↑` | Move frog up      |
| `↓` | Move frog down    |
| `←` | Move frog left    |
| `→` | Move frog right   |
| `P` | Pause/Play game   |
| `N` | Start New Game    |
| `S` | Settings          |
| `H` | High Scores       |
| `E` | Exit main menu    |
| `M` | Back to main menu |

---

## 🙏 Acknowledgments

- **Sean** - Many thanks for providing the robust game engine framework that served as the foundation for this project
- **COMP2011J Teaching Team** - For guidance and support throughout the development
- **JavaFX Community** - For excellent UI development resources

---

## 📜 License

This project is created for educational purposes as part of COMP2011J assignment.

---

**Happy Gaming!** 🐸✨