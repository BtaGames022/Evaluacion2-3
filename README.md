# LevelUp Gamer 🎮

**Asignatura:** Desarrollo de Aplicaciones Móviles (DSY1105)  
**Evaluación:** Parcial 4 - Integración y Despliegue

## 📋 Descripción del Proyecto

**LevelUp Gamer** es una solución móvil integral de comercio electrónico diseñada para la comunidad gamer. El proyecto implementa una arquitectura moderna basada en microservicios y consumo de APIs, cumpliendo con estándares de desarrollo profesional.

La aplicación permite a los usuarios navegar por un catálogo de productos, gestionar un carrito de compras con persistencia local, leer novedades desde un blog externo y acceder a ofertas exclusivas gestionadas por nuestro propio backend.

---

## 🚀 Características Principales

Este proyecto integra las siguientes funcionalidades clave requeridas para la evaluación:

### 1. 🔗 Arquitectura y Conectividad
* **MVVM (Model-View-ViewModel):** Separación clara de lógica de negocio e interfaz.
* **API REST Externa:** Consumo de `jsonplaceholder` para la sección de "Blog" mediante **Retrofit**.
* **Microservicio Propio:** Conexión a un backend **Spring Boot** (Java) que gestiona las "Ofertas Especiales".

### 2. 💾 Persistencia de Datos
* **Room Database:** Almacenamiento local seguro para usuarios y el carrito de compras.
* **DataStore:** Gestión de sesión de usuario (Login/Logout).

### 3. 🛡️ Calidad y Seguridad
* **Pruebas Unitarias:** Validación de lógica de negocio (ViewModels) usando JUnit y MockK.
* **APK Firmado (Release):** Generación de ejecutable optimizado con `minifyEnabled` y ofuscación mediante Proguard.

---

## 🛠️ Stack Tecnológico

### App Móvil (Android)
* **Lenguaje:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material Design 3)
* **Red:** Retrofit 2 + Gson Converter
* **Persistencia:** Room + DataStore
* **Concurrencia:** Coroutines & Flow
* **Testing:** JUnit 4/5, MockK, Kotest

### Backend (Microservicio)
* **Framework:** Spring Boot 3.4.11
* **Lenguaje:** Java 17
* **Base de Datos:** H2 Database (En memoria)
* **Persistencia:** Spring Data JPA

---

## 📸 Galería del Proyecto

### 📱 1. Interfaz de Usuario (UI)

Evidencia funcional de las pantallas principales de la aplicación.

| Pantalla de Inicio | Ofertas (Microservicio) | Carrito de Compras |
|:---:|:---:|:---:|
| ![Pantalla Principal](<img width="593" height="968" alt="Captura de pantalla 2025-11-17 054858" src="https://github.com/user-attachments/assets/798197a4-f17d-4be7-840a-3638dc869bdb" />
) | ![Ofertas Backend](<img width="590" height="872" alt="Captura de pantalla 2025-11-17 054945" src="https://github.com/user-attachments/assets/00c02b61-1008-4286-a887-68930e454742" />
) | ![Carrito](<img width="585" height="877" alt="Captura de pantalla 2025-11-17 055044" src="https://github.com/user-attachments/assets/9dec1c5a-b967-4e15-bc1d-35588e67d020" />
) |

### ⚙️ 2. Evidencia del Backend

Demostración del microservicio Spring Boot en ejecución.

| Ejecución en Consola (Puerto 8080) | Estructura del Proyecto Backend |
|:---:|:---:|
| ![Consola Backend](<img width="1861" height="805" alt="Captura de pantalla 2025-11-17 050934" src="https://github.com/user-attachments/assets/81e6eb8c-4e46-408a-bf22-3995ee186d2b" />
) | ![Estructura IDE](<img width="1865" height="240" alt="Captura de pantalla 2025-11-17 051011" src="https://github.com/user-attachments/assets/799dfae6-ee00-41dc-9dba-64c19cd17c78" />
) |

---

## 📦 3. Proceso de Generación de APK Firmado

Evidencia paso a paso de la configuración y generación del archivo `.apk` en modo **release** (IL 3.3.1).

| Configuración de Firma (Keystore) | Selección de Build Variant (Release) | Generación Exitosa | Archivo Final Generado |
|:---:|:---:|:---:|:---:|
| ![Keystore Config](<img width="1919" height="1079" alt="Captura de pantalla 2025-11-17 044828" src="https://github.com/user-attachments/assets/5d9d9fc6-6be0-4643-a319-02cb56c8aa41" />
) | ![Build Variant](<img width="1919" height="1079" alt="Captura de pantalla 2025-11-17 044846" src="https://github.com/user-attachments/assets/476835ab-b807-4ca2-a92e-51b872d04459" />
) | ![Build Success](<img width="457" height="140" alt="Captura de pantalla 2025-11-17 045330" src="https://github.com/user-attachments/assets/d7263db0-9d51-488a-8254-d10b13a28cc8" />
) | ![APK File](<img width="1317" height="652" alt="Captura de pantalla 2025-11-17 050815" src="https://github.com/user-attachments/assets/80b43d55-64db-4c0c-80f5-ea0d601fbc5b" />
) |

> **Nota:** El APK generado cuenta con ofuscación de código (`minifyEnabled = true`) y reducción de recursos (`shrinkResources = true`) para optimizar su rendimiento y seguridad.

---

## 🔧 Instrucciones de Instalación y Ejecución

Para probar el sistema completo (App + Backend), siga estos pasos:

### 1. Levantar el Microservicio (Backend)
1.  Abrir el proyecto `backend` en IntelliJ IDEA.
2.  Ejecutar la clase `BackendApplication.java`.
3.  Esperar a ver el mensaje: `Tomcat started on port 8080`.
    * *Endpoint de prueba:* `http://localhost:8080/products`

### 2. Ejecutar la App Móvil
1.  Abrir el proyecto `LevelUpGamer3` en Android Studio.
2.  Sincronizar el proyecto con Gradle.
3.  Ejecutar en un Emulador (Configurado para `http://10.0.2.2:8080/`) o Dispositivo Físico (Configurar IP local).

---

## 🧪 Testing

Las pruebas unitarias se encuentran en la carpeta `src/test/java`. Para ejecutarlas:
1.  Clic derecho en la carpeta `com.levelupgamer.app (test)`.
2.  Seleccionar "Run Tests in 'com.levelupgamer.app'".

---

**Autores:** Sebastian Altamirano y Joaquin Allendes.
