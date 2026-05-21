# 🍽️ RestoManager - Aplicación de Gestión de Restaurante

## 📋 Tabla de Contenidos
- [Descripción](#descripción)
- [Características](#características)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Arquitectura](#arquitectura)
- [Instalación](#instalación)
- [Uso](#uso)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Módulos Principales](#módulos-principales)
- [Dependencias](#dependencias)
- [Contribución](#contribución)

---

## 📱 Descripción

**RestoManager** es una aplicación móvil desarrollada en **Kotlin** para Android que facilita la gestión integral de un restaurante. Fue creada como proyecto final para la materia "Desarrollo de Aplicaciones Móviles 1" y proporciona funcionalidades completas desde la autenticación de usuarios hasta la gestión de pedidos, mesas y facturación.

La aplicación está diseñada para mejorar la experiencia tanto del personal del restaurante como de los clientes, permitiendo un control eficiente de operaciones diarias.

---

## ✨ Características

### 👤 Autenticación y Seguridad
- **Login y Registro**: Autenticación segura mediante Firebase Authentication
- **Gestión de sesiones**: Validación de credenciales de usuario
- **Control de acceso**: Diferentes perfiles de usuario (administrador, camarero, etc.)

### 🍜 Gestión de Menú
- **Catálogo de alimentos**: Visualización interactiva del menú
- **Información de platos**: Descripción, precio y disponibilidad
- **Carga de imágenes**: Visualización de fotos de los platos usando Glide

### 🛒 Gestión de Pedidos
- **Creación de pedidos**: Interfaz intuitiva para realizar pedidos
- **Carrito de compras**: Resumen de artículos seleccionados
- **Historial de pedidos**: Seguimiento de órdenes realizadas

### 📊 Gestión de Inventario
- **Lista de productos**: CRUD (Crear, Leer, Actualizar, Eliminar) de productos
- **Agregar/Editar productos**: Interfaz para administrar el inventario
- **Control de stock**: Disponibilidad de artículos

### 🪑 Gestión de Mesas
- **Lista de mesas**: Visualización del estado de todas las mesas
- **Agregar/Editar mesas**: Administración de espacios disponibles
- **Estado de mesas**: Ocupada, disponible, reservada

### 💰 Facturación
- **Generación de facturas**: Resumen de transacciones
- **Cálculo de totales**: Suma de productos y aplicación de impuestos
- **Historial de pagos**: Registro de transacciones completadas

---

## 🛠️ Tecnologías Utilizadas

### Lenguaje y Framework
- **Kotlin**: Lenguaje principal del proyecto (100%)
- **Android SDK**: Desarrollo para dispositivos Android

### Base de Datos y Backend
- **Firebase Authentication**: Autenticación de usuarios
- **Firebase Realtime Database**: Base de datos en tiempo real
- **Firebase Firestore**: Base de datos NoSQL en la nube
- **Firebase Storage**: Almacenamiento de imágenes y archivos

### Librerías y Herramientas
- **AndroidX**: Librerías de compatibilidad de Android
- **Glide**: Carga y caché de imágenes
- **Material Design**: Componentes UI modernos
- **View Binding**: Enlace seguro de vistas

### Configuración del Proyecto
- **Gradle**: Sistema de compilación
- **SDK Mínimo**: Android 7.0 (API 24)
- **SDK Objetivo**: Android 15 (API 36)
- **Versión de Java**: 11

---

## 🏗️ Arquitectura

### Patrón de Diseño: MVC (Model-View-Controller)
La aplicación sigue el patrón MVC con componentes claramente definidos:

```
┌─────────────────────────────────────┐
│         UI Layer (Activities)        │
│  (LoginActivity, MainActivity, etc)  │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│      Business Logic Layer           │
│  (Lógica de negocio y validaciones) │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│      Data Layer (Firebase)          │
│  (Base de datos y autenticación)    │
└─────────────────────────────────────┘
```

### Componentes Principales

#### View (UI)
- **Activities**: Presentan la interfaz de usuario
- **View Binding**: Enlace seguro con las vistas XML
- **Material Design**: Interfaz moderna y responsiva

#### Model (Datos)
- **Firebase Firestore**: Almacenamiento de datos estructurados
- **Firebase Realtime Database**: Sincronización en tiempo real
- **POJOs/Data Classes**: Modelos de datos Kotlin

#### Controller (Lógica)
- **Activity Logic**: Manejo de eventos y navegación
- **Firebase SDKs**: Gestión de operaciones de datos
- **Validaciones**: Lógica de negocio y reglas de aplicación

---

## 📦 Instalación

### Requisitos Previos
- **Android Studio** versión más reciente
- **JDK 11** o superior
- **Git** instalado en tu sistema
- **Cuenta de Firebase** para configurar el backend

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/ROBER-SALAMA/restoManager.git
   cd restoManager
   ```

2. **Abrir en Android Studio**
   - Abre Android Studio
   - Selecciona "Open an Existing Project"
   - Navega a la carpeta clonada

3. **Sincronizar Gradle**
   - Android Studio sincronizará automáticamente las dependencias
   - Si no lo hace, ve a: `File → Sync Now`

4. **Configurar Firebase**
   - Ve a [Firebase Console](https://console.firebase.google.com/)
   - Crea un nuevo proyecto
   - Descarga el archivo `google-services.json`
   - Coloca el archivo en: `app/`
   - Habilita los servicios necesarios:
     - Authentication (Email/Password)
     - Firestore Database
     - Realtime Database
     - Storage

5. **Ejecutar la Aplicación**
   - Conecta un dispositivo Android o inicia un emulador
   - Haz clic en "Run" o presiona `Shift + F10`

---

## 🚀 Uso

### Flujo de Trabajo de la Aplicación

#### 1. **Autenticación**
   - El usuario accede a `LoginActivity`
   - Inicia sesión con correo y contraseña
   - Si no tiene cuenta, puede registrarse en `RegisterActivity`

#### 2. **Pantalla Principal**
   - Una vez autenticado, accede a `MainActivity`
   - Visualiza opciones del menú principal
   - Navega a diferentes módulos

#### 3. **Gestión del Menú**
   - Accede a `FoodMenuActivity`
   - Visualiza los platos disponibles
   - Selecciona artículos para agregar al carrito

#### 4. **Realizar Pedido**
   - En `SummaryActivity` revisa el resumen de compras
   - Confirma los artículos seleccionados
   - Procesa el pago

#### 5. **Administración (Admin)**
   - Accede a `ProductListActivity` para gestionar productos
   - Usa `AddEditProductActivity` para crear/editar productos
   - Gestiona mesas en `TableListActivity`
   - Revisa órdenes en `OrderListActivity`
   - Genera facturas en `BillingActivity`

---

## 📂 Estructura del Proyecto

```
restoManager/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/uma/sistema_restaurante/
│   │   │   │   ├── LoginActivity.kt              # Pantalla de login
│   │   │   │   ├── RegisterActivity.kt           # Registro de usuarios
│   │   │   │   ├── MainActivity.kt               # Pantalla principal
│   │   │   │   ├── FoodMenuActivity.kt           # Visualización del menú
│   │   │   │   ├── SummaryActivity.kt            # Resumen de pedidos
│   │   │   │   ├── ProductListActivity.kt        # Lista de productos (admin)
│   │   │   │   ├── AddEditProductActivity.kt     # Agregar/Editar productos
│   │   │   │   ├── OrderListActivity.kt          # Lista de órdenes
│   │   │   │   ├── BillingActivity.kt            # Facturación
│   │   │   │   ├── TableListActivity.kt          # Gestión de mesas
│   │   │   │   └── AddEditTableActivity.kt       # Agregar/Editar mesas
│   │   │   ├── res/
│   │   │   │   ├── layout/                       # Archivos XML de interfaces
│   │   │   │   ├── drawable/                     # Imágenes y vectores
│   │   │   │   ├── values/                       # Strings, colores, estilos
│   │   │   │   └── menu/                         # Menús de opciones
│   │   │   └── AndroidManifest.xml               # Configuración de la app
│   │   ├── test/                                 # Pruebas unitarias
│   │   └── androidTest/                          # Pruebas de instrumentación
│   ├── build.gradle.kts                          # Configuración de compilación
│   └── proguard-rules.pro                        # Reglas de ofuscación
├── build.gradle.kts                              # Configuración raíz
├── settings.gradle.kts                           # Configuración de módulos
├── gradle.properties                             # Propiedades de Gradle
├── gradlew / gradlew.bat                         # Wrapper de Gradle
└── README.md                                     # Este archivo
```

---

## 🔧 Módulos Principales

### 1. **Autenticación** (`LoginActivity`, `RegisterActivity`)
```kotlin
// Ejemplo: Verificar autenticación
fun loginUser(email: String, password: String) {
    FirebaseAuth.getInstance()
        .signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Usuario autenticado, ir a MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Mostrar error
                Toast.makeText(this, "Error de autenticación", Toast.LENGTH_SHORT).show()
            }
        }
}
```

### 2. **Gestión de Productos** (`ProductListActivity`, `AddEditProductActivity`)
```kotlin
// Ejemplo: Agregar producto a Firestore
fun addProduct(product: Product) {
    val db = FirebaseFirestore.getInstance()
    db.collection("products")
        .add(product)
        .addOnSuccessListener { documentReference ->
            Log.d("TAG", "Producto agregado: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w("TAG", "Error al agregar producto", e)
        }
}
```

### 3. **Gestión de Pedidos** (`SummaryActivity`, `OrderListActivity`)
- Almacena pedidos en Firebase Realtime Database
- Sincronización en tiempo real
- Cálculo de totales y aplicación de impuestos

### 4. **Facturación** (`BillingActivity`)
- Genera resúmenes de transacciones
- Registra pagos completados
- Historial de facturas

---

## 📚 Dependencias

Las principales dependencias están definidas en `app/build.gradle.kts`:

| Dependencia | Versión | Propósito |
|------------|---------|----------|
| androidx.core.ktx | Latest | Extensiones de Kotlin para Android |
| androidx.appcompat | Latest | Compatibilidad hacia atrás |
| material | Latest | Componentes Material Design |
| Firebase BOM | Latest | Gestión de versiones Firebase |
| firebase-auth | Latest | Autenticación de usuarios |
| firebase-firestore | Latest | Base de datos NoSQL |
| firebase-database | Latest | Base de datos en tiempo real |
| firebase-storage | Latest | Almacenamiento de archivos |
| glide | Latest | Carga de imágenes |

---

## 🔐 Mejores Prácticas Implementadas

✅ **Separación de Responsabilidades**: Cada Activity gestiona su propia lógica
✅ **View Binding**: Uso seguro de referencias a vistas
✅ **Firebase SDK**: Autenticación y almacenamiento seguros
✅ **Manejo de Errores**: Try-catch y callbacks apropiados
✅ **Logs**: Registro de eventos importantes
✅ **Restricciones de Permisos**: Solicitud de permisos en Android 6+
✅ **Versioning**: Control de versiones con Git

---

## 🤝 Contribución

Para contribuir al proyecto:

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commits tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📝 Notas para Desarrolladores

### Configuración de Entorno
- Asegúrate de tener configurado el SDK de Android correctamente
- Verifica que la versión de Kotlin sea compatible (1.9.0+)
- Ten acceso a Firebase para pruebas

### Debugging
- Usa Logcat en Android Studio para revisar logs
- Activa Firebase Remote Config para debugging remoto
- Prueba en múltiples versiones de Android

### Performance
- Monitorea el uso de memoria en el Profiler
- Optimiza consultas a Firestore para reducir costos
- Usa lazy loading para listas grandes

---

## 📄 Licencia

Este proyecto fue desarrollado como trabajo de curso. Ver `LICENSE` para más detalles.

---

## 👨‍💻 Autor

**Roberto Salama** - [ROBER-SALAMA](https://github.com/ROBER-SALAMA)

**Jacqueline Cruz**- [yaquiC](https://github.com/yaquiC)

**Cesar Leiva** - [Cesar-Leiva](https://github.com/Cesar-Leiva)

**Flor Hernandez**

Proyecto Final - Desarrollo de Aplicaciones Móviles 1

---

## 🆘 Soporte

Para reportar bugs o sugerir mejoras, abre un [Issue](https://github.com/ROBER-SALAMA/restoManager/issues) en el repositorio.

---

**Última actualización**: Mayo 2026
