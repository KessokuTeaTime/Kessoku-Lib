### <p align=right>[`→` Docs](https://kessokuteatime.github.io/documentation/lib-docs/)&ensp;[`→` CurseForge](https://www.curseforge.com/minecraft/mc-mods/kessoku-lib)&ensp;[`→` Modrinth](https://modrinth.com/mod/kessoku-lib)</p>

# Kessoku Lib

###### The next generation cross-platform modular Minecraft modding API.

[![fabric](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/fabric_vector.svg)](https://fabricmc.net/)
[![neoforge](https://raw.githubusercontent.com/KessokuTeaTime/Badges-Extra/main/assets/cozy/supported/neoforge_vector.svg)](https://neoforged.net/)
[![forge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/unsupported/forge_vector.svg)](https://minecraftforge.net/)

<details>
  <summary>
    <sub><a href="https://cloudsmith.com">
      <img src="https://img.shields.io/badge/OSS%20Hosting%20by-Cloudsmith-blue?logo=cloudsmith&style=flat-square"  alt=""/>
    </a></sub>
  </summary>
  <blockquote>
    Our package repository hosting is graciously provided by <a href="https://cloudsmith.com">Cloudsmith</a>.
    Cloudsmith is the only fully hosted, cloud-native, universal package management solution, that enables organizations to create, store and share packages in any format, to any place, with total confidence.
  </blockquote>
</details>

## What is this?
KessokuLib is a cross-platform modular library for Minecraft: Java Edition.

It is intended to provide a unified intermediate API between mod loaders (we don't provide anything like Connector, I don't see the benefit of providing something like Connector, on the contrary it would probably bother mod developers more).

We have incorporated the benefits of each platform's API into this mod's API in order to simplify mod development.

## Why do you need a cross-platform API?
This means that most of your code is generic and only some of it is individually specific to a platform, which is even more important for developing.

One Code, Each Platform. -- By H2Sxxa

## What is the current stage of development of this library?
Still in WIP.

## What about the language loader?
Kessoku Languages is currently in EAP status, but all we can guarantee at the moment is that we can provide language loaders with Kotlin/Scala/Groovy.