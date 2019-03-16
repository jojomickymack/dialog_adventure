# Dialog Adventure

![dialog_adventure.gif](.github/dialog_adventure.gif?raw=true)

This is my best game so far, it uses LibGdx Ashley and makes a good example of an entity component system combined with Tiled maps and collision detection.

It's one of the only games I've done that has multiple screens that bind to the input listener and clear and load stages that are shared throughout the game with their own elements.

The game screen has an on screen gamepad, and physics gamepads and keyboard controls will work as well. The enemies in this game don't hurt you, but trigger dialog boxes to appear where each enemy's 'script' will be displayed.

When the dialog box appears, the game goes into 'dialog mode' where touches will advance the text. Special treatment went into making it so you have to move away from the enemy that triggered the dialog and then collide with him again to re-trigger the dialog again.

The limitation I realized I was running into was how to add more types of characters and have each one say different things. This will turn into a huge mess if their scripts are in the source code - so I need my next version of this to load scripts from some other format outside the source folder.

The best solution for this problem, for me at least, is to use [yarn](https://github.com/InfiniteAmmoInc/Yarn) to store all the dialogs. You 
can load them into your libgdx game using [yarngdx](https://github.com/kyperbelt/YarnGdx).
