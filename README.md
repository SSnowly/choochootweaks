# ChooChoo Tweaks

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.7-green.svg)](https://minecraft.net)
[![Fabric](https://img.shields.io/badge/Mod%20Loader-Fabric-blue.svg)](https://fabricmc.net)
[![Release](https://img.shields.io/github/v/release/SSnowly/choochootweaks)](https://github.com/SSnowly/choochootweaks/releases)

A Minecraft Fabric mod that allows you to configure minecart speeds for different types of minecarts. Take full control of your railway systems!

## ✨ Features

- **Configurable Speeds**: Set different speeds for each type of minecart
- **Command Support**: Configure speeds via commands with `/minecartspeed`
- **Per-World Settings**: Each world maintains its own speed configuration
- **Real-Time Updates**: Changes apply immediately without restart
- **Operator Permissions**: Only operators can modify speeds for server safety
- **Auto-Save**: Changes are automatically saved when made by operators

## 🚂 Supported Minecart Types

- Empty Minecart
- Minecart with Player
- Minecart with Entity
- Chest Minecart
- Furnace Minecart
- Hopper Minecart
- TNT Minecart
- Spawner Minecart
- Command Block Minecart

## 📥 Installation

1. Make sure you have [Fabric Loader](https://fabricmc.net/use/installer/) installed
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) for your Minecraft version
3. Download the latest release from the [Releases page](https://github.com/SSnowly/choochootweaks/releases)
4. Place the downloaded `.jar` file in your `.minecraft/mods` folder
5. Launch Minecraft with the Fabric profile

## 🎮 Usage

### Command Usage
```
/minecartspeed set <type> <speed>    # Set speed for a minecart type
/minecartspeed get <type>            # Get current speed for a type
/minecartspeed list                  # List all current speeds
/minecartspeed reset                 # Reset all speeds to defaults
```

**Examples:**
```
/minecartspeed set empty_minecart 1.0
/minecartspeed get chest_minecart
/minecartspeed list
```

## ⚙️ Requirements

- **Minecraft**: 1.21.7
- **Fabric Loader**: 0.17.2 or higher
- **Fabric API**: 0.129.0+1.21.7 or higher
- **Java**: 21 or higher

## 🛠️ For Developers

### Building from Source
```bash
git clone https://github.com/SSnowly/choochootweaks.git
cd choochootweaks
./gradlew build
```

Built files will be in `build/libs/`:
- `choochoo-tweaks-<version>.jar` - Main mod file
- `choochoo-tweaks-<version>-sources.jar` - Source code

### Contributing
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📝 Configuration

Configuration files are stored per-world in `saves/<world>/choochoo_tweaks_speeds.json`. The file is automatically created and managed by the mod.

Example configuration:
```json
{
  "empty_minecart": 0.4,
  "minecart_with_player": 0.4,
  "chest_minecart": 0.4,
  "furnace_minecart": 0.4,
  "hopper_minecart": 0.4,
  "tnt_minecart": 0.4,
  "spawner_minecart": 0.4,
  "command_block_minecart": 0.4,
  "minecart_with_entity": 0.4
}
```

## 🐛 Issues & Support

Found a bug or have a feature request? Please open an issue on the [GitHub Issues page](https://github.com/SSnowly/choochootweaks/issues).

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**SSnowly**
- GitHub: [@SSnowly](https://github.com/SSnowly)

## 🙏 Acknowledgments

- Thanks to the Fabric team for the excellent modding framework
- Thanks to the Minecraft modding community for inspiration and support
