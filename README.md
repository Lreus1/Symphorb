# Symphorb 🎯

**Symphorb** es un juego de físicas 2D tipo *Pachinko* con mecánicas estratégicas inspiradas en títulos como *Peglin* y *Slay the Spire*,
desarrollado con Jetpack Compose y arquitectura MVVM en Android Studio. El jugador lanza esferas que rebotan en un tablero interactivo lleno de pines, 
recolectando monedas, activando habilidades y venciendo enemigos.

---

## 🚀 Estado del Proyecto

Actualmente se encuentra en desarrollo activo.

**Fases completadas:**

- ✅ FASE 1: Sistema base de lanzamiento y puntuación
- ✅ FASE 2: Arquitectura MVVM modularizada
- ✅ FASE 3: Sistema `drag and shoot` + visualización precisa de trayectoria

**Fases en desarrollo:**

- 🔧 FASE 4: Múltiples niveles, rebotes avanzados y selector de nivel
- 🧪 FASE 5: Física híbrida con colisiones frame-by-frame y sistema de reliquias
- 🎨 FASE 6: Implementación visual con sprites pixel art, animaciones, sonidos

---

## 🧠 Estructura del Proyecto

```plaintext
com.example.symphorb
├── Bola.kt                         # Modelo físico de la esfera
├── SimuladorPachinko.kt           # Motor de rebotes simplificado
├── ui/
│   ├── PachinkoView.kt            # Vista principal del juego
│   ├── PachinkoBoard.kt           # Lógica visual y física del tablero
│   ├── DragAndShoot.kt            # Mecanismo drag-to-shoot con trayectoria visual
│   ├── PantallaInicio.kt          # Pantalla de inicio
│   ├── HistorialView.kt           # Visualización del historial de puntuaciones
│   └── DireccionSelectorView.kt   # Selector direccional de lanzamiento
├── viewmodel/
│   ├── RecordViewModel.kt         # Puntuaciones y estado del jugador
│   └── RecordViewModelFactory.kt  # Factory para ViewModel
```

---

## 🛠️ Cómo compilar

1. Clona el repositorio:

```bash
git clone https://github.com/Lreus1/Symphorb.git
```

2. Ábrelo en Android Studio.
3. Compila el proyecto (Gradle `kts`, requiere mínimo **Android API 28+**).
4. Ejecuta en un emulador o dispositivo físico (se recomienda 60 FPS).

---

## 🧭 Filosofía del Proyecto

Este juego nace del deseo de explorar las matemáticas del rebote, la elegancia de la simulación física y la belleza de la interacción precisa. No se trata solo de lanzar una bola, sino de entender cómo se mueve el mundo que hemos creado para ella.

> “Vengo de muy lejos, voy más lejos aún.” — *Erik Urano*

**Principios del autor:**
- Crear con intención, compartir con apertura.
- Fomentar la curiosidad, el aprendizaje y la gratitud.
- Rechazar el cinismo, abrazar la mejora continua.
- Respeto absoluto a todas las formas de vida y creatividad.

---

## 📜 Licencia

Este proyecto está licenciado bajo la [MIT License](LICENSE).

Puedes utilizarlo, modificarlo y compartirlo libremente. Si construyes algo con él, ¡nos encantaría saberlo!

---

## 📬 Contacto

Proyecto desarrollado por **Lreus1**  
Visita: [github.com/Lreus1](https://github.com/Lreus1)  
Colabora, comparte y juega. Este mundo lo estamos construyendo juntos.
