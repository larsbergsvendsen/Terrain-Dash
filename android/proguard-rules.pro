# LibGDX ProGuard rules
-keep class com.badlogic.gdx.** { *; }
-keep class com.badlogic.gdx.backends.android.** { *; }
-keep class com.badlogic.gdx.physics.box2d.** { *; }

# Keep game classes
-keep class com.terraindash.** { *; }

# General Android
-dontwarn android.**
-dontwarn com.badlogic.gdx.jnigen.**
