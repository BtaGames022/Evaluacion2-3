# --- REGLAS PARA LEVEL UP GAMER ---

# 1. Mantener los Modelos de Datos (Para que Gson pueda leer el JSON)
# Esta regla es crucial. Mantiene las clases en el paquete 'model' y, más importante,
# mantiene los nombres de sus campos, lo cual es necesario para que la deserialización
# de JSON funcione correctamente.
-keepclassmembers class com.levelupgamer.app.model.** { *; }
-keep public class com.levelupgamer.app.model.**

# 2. Mantener Retrofit y las llamadas a API
-keep interface com.levelupgamer.app.data.remote.** { *; }
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-keepattributes Signature
-keepattributes *Annotation*

# 3. Mantener Room (Base de Datos)
-keep class androidx.room.** { *; }
-dontwarn androidx.room.paging.**
