# 🏋️ HabitTrack
### Registro de Hábitos y Rutinas
**Programación en Móviles — Trabajo Final Parte 1**

---

## 👥 Equipo

| Integrante | Rol |
|---|---|
| Alexander Sanabria | Lógica base, arquitectura MVVM, API ZenQuotes |
| Harold Santivañez | Rediseño UI, glassmorphism dark theme, integración Git |

---

## 📱 Descripción

HabitTrack es una aplicación Android para registrar, visualizar y mantener hábitos diarios y semanales. Permite llevar un historial de cumplimiento por categoría (gimnasio, lectura, hidratación, meditación, idiomas) y recibe frases motivacionales desde una API pública.

---

## 🛠️ Stack Tecnológico

| Tecnología | Uso |
|---|---|
| Kotlin | Lenguaje principal |
| Jetpack Compose | UI declarativa |
| MVVM + Repository | Arquitectura |
| Room (SQLite) | Persistencia local |
| Retrofit 2 | Consumo de API REST |
| Navigation Compose | Navegación entre pantallas |
| ZenQuotes API | Frases motivacionales |

---

## 🏗️ Arquitectura MVVM

La app sigue estrictamente el patrón **MVVM (Model–View–ViewModel)** combinado con el **Repository Pattern**:

- **View (Composables):** Solo contiene código de UI. Observa el estado mediante `collectAsState()` y reacciona a cambios por recomposición. Sin lógica de negocio.
- **ViewModel:** Expone el estado mediante `StateFlow<HabitUiState>`. Lanza corrutinas en `viewModelScope`. Solo interactúa con el Repository.
- **Repository:** Única fuente de verdad. Decide si los datos vienen de Room (local) o Retrofit (API remota).

```
View (Composables)
    ↕ collectAsState()
ViewModel (StateFlow)
    ↕ suspend functions
Repository
    ↕               ↕
Room (local)    Retrofit (API)
```

---

## 📂 Estructura de Paquetes

```
com.habittrack/
├── data/
│   ├── local/         → HabitEntity, HabitLogEntity, HabitDao, AppDatabase
│   ├── model/         → Habit.kt
│   └── remote/        → ZenQuotesApiService.kt, QuoteDto.kt
├── domain/
│   └── repository/    → HabitRepository.kt
├── ui/
│   ├── navigation/    → NavGraph.kt
│   ├── screens/       → HabitListScreen, HabitDetailScreen, HabitFormScreen, StatsScreen
│   └── theme/         → Color.kt, Theme.kt, Type.kt
├── viewmodel/         → HabitViewModel.kt
└── MainActivity.kt
```

---

## 📸 Capturas de Pantalla

### Pantalla 1 — Lista de Hábitos
> Pantalla principal con todos los hábitos del día, indicador de progreso y color por categoría.

<img src="screenshots/habit_list.png" width="280"/>

---

### Pantalla 2 — Nuevo Hábito (Formulario)
> Formulario para crear o editar un hábito con selector de categoría en grid visual.

<img src="screenshots/habit_form.png" width="280"/>

---

### Pantalla 3 — Detalle del Hábito
> Información completa del hábito: estado del día, frecuencia, fecha de creación y frase motivacional.

<img src="screenshots/habit_detail.png" width="280"/>

---

### Pantalla 4 — Estadísticas y Motivación
> Progreso del día con anillo circular, métricas por categoría y frase motivacional de ZenQuotes API.

<img src="screenshots/stats.png" width="280"/>

---

## 🗄️ Modelo de Datos — Room

### HabitEntity
| Campo | Tipo | Descripción |
|---|---|---|
| id | Int (PK) | Autoincremental |
| name | String | Nombre del hábito |
| category | String | Categoría del hábito |
| color | String | Color hex asignado |
| frequencyDays | String | Días activos en JSON |
| createdAt | Long | Timestamp de creación |
| isActive | Boolean | Permite archivar sin eliminar |

### HabitLogEntity
| Campo | Tipo | Descripción |
|---|---|---|
| id | Int (PK) | Autoincremental |
| habitId | Int (FK) | Referencia a HabitEntity |
| date | String | Fecha ISO yyyy-MM-dd |
| completed | Boolean | Si fue completado ese día |
| notes | String? | Nota opcional |

---

## 🌐 API — ZenQuotes

| Propiedad | Valor |
|---|---|
| Base URL | `https://zenquotes.io/api/` |
| Endpoint | `GET /random` |
| Autenticación | No requerida |
| Respuesta | `[{"q": "frase", "a": "autor"}]` |

**Manejo de estados:**
- ⏳ **Loading:** `CircularProgressIndicator` mientras carga
- ✅ **Success:** Frase y autor en card con diseño glassmorphism
- ❌ **Error:** Mensaje descriptivo + botón Reintentar

---

## 🧭 Navegación

| Ruta | Pantalla | Argumentos |
|---|---|---|
| `habits/list` | HabitListScreen | Ninguno |
| `habits/{habitId}` | HabitDetailScreen | `habitId: Int` |
| `habits/form?habitId={habitId}` | HabitFormScreen | `habitId: Int?` (null = crear) |
| `stats` | StatsScreen | Ninguno |

---

## ✅ Requisitos Funcionales Implementados

| ID | Requisito | Estado |
|---|---|---|
| RF-01 | Gestión de hábitos (CRUD) | ✅ |
| RF-02 | Registro de cumplimiento | ✅ |
| RF-03 | Listado de hábitos | ✅ |
| RF-04 | Detalle del hábito | ✅ |
| RF-05 | Formulario crear/editar | ✅ |
| RF-06 | Pantalla de Estadísticas | ✅ |
| RF-07 | Consumo de API ZenQuotes | ✅ |
| RF-08 | Manejo de estados de red | ✅ |

---

## 🚀 Cómo ejecutar el proyecto

1. Clonar el repositorio:
```bash
git clone https://github.com/alexandersanabria-lang/HabitTrack.git
```
2. Abrir en **Android Studio**
3. Sincronizar Gradle
4. Correr en emulador o dispositivo físico (min SDK 26)

---

*HabitTrack — Programación en Móviles — Tecsup 2026*