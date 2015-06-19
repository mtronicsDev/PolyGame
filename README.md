![Polygame Logo](http://mtronicsdev.github.io/PolyGame/images/polygame.png "Polygame")
## A Java™ 3D open source game engine.
### The goal
The goal of me developing this game engine is to make a game engine that is packed full of features and technology while still being incredibly easy to develop games with.

### Under the hood
This engine is using [LWJGL 3](https://github.com/LWJGL/lwjgl3) to handle graphics and audio and is designed to be expandable, modular and future proof.

### Features
Polygame is still a work in progress but many key features are already fully working. Here is a quick list of the most important ones:
* Modular Entities
  * Every object in the world is represented by an __entity__.
  * Entities can (and most likely _will_) contain __modules__.
  * Modules are the building blocks that make up entities. They can be e.g. a camera, model, audio source, AI, controller, ...
  * Of course you can write _your own_ modules, but there is a number of them already included.
* Multi-window support
  * Since Polygame is using LWJGL 3 whose window manager is GLFW, it's easy to create multiple windows!
  * Windows share the same world but they of course _can show completely different things_.
* Built-in terrain, water and skybox modules
  * This allows your game to _get pretty very fast_.
  * Terrains can be loaded from heightmaps or can be generated at runtime.
  * They also support multiple textures, so your beaches, rocks and paths can go right onto the terrain.
  * Water uses _reflection, refraction and wave effects_ to appear _realistic_.
  * Skyboxes are loaded from six individual images and surround the whole field of view of the bound camera entity.
* Easy resource loading
  * __One central class__ handles all the resource loading.
  * __Resource handlers__ can be added to this class to load custom resource types.
