# 🌋 Proyecto: Floor is Lava (Node.js)

Aquest projecte consisteix en un joc de consola interactiu on el jugador ha de navegar per un tauler de **8x6** (files A-F, columnes 0-7). L'objectiu és arribar al tresor sense esgotar els punts de vida en trepitjar les 16 caselles de lava ocultes.

---

## 📂 Estructura del Codi

### 1. `index.js` (Interfície i Control)
És el punt d'entrada del programa. Gestiona la comunicació entre l'usuari i la lògica del joc.
* **Mòdul `readline`**: S'utilitza per llegir les ordres de l'usuari des de la terminal sense bloquejar l'execució.
* **Bucle de Joc**: La funció `demanarComanda()` es crida a si mateixa recursivament per mantenir el joc actiu fins que es guanya o es perd.
* **Intèrpret de comandes**: Utilitza un `switch` per processar ordres com `caminar`, `guardar`, `activar trampa`, etc.

### 2. `gameloop.js` (Motor Lògic)
Conté la classe `GameLoop` que encapsula totes les regles:
* **`generarLava()`**: Genera 16 posicions aleatòries. Inclou una validació per evitar que la lava aparegui a l'inici (**A0**) o al final (**F7**).
* **`moure(direccio)`**: 
    * Calcula la nova coordenada.
    * Valida si el jugador surt del tauler (**Penyasegat**).
    * Comprova si la casella té lava (resta 1 punt i la destapa).
    * Comprova si s'ha arribat al destí.
* **`getDistanciaLava()`**: Implementa la **Distància de Manhattan** per calcular la proximitat de la lava més propera i guiar el jugador.
* **`render(revelar)`**: Genera la representació visual del tauler en format text. El paràmetre `revelar` permet veure la lava oculta (Mode Trampa).
* **Gestió de Fitxers**: Mètodes `guardar()` i `carregar()` que utilitzen `fs.writeFileSync` i `fs.readFileSync` per gestionar fitxers `.json`.

### 3. `package.json`
Defineix el projecte i inclou l'script `"start": "node index.js"`, permetent l'execució estàndard mitjançant **npm**.

---

## 🕹️ Mecàniques del Joc

| Acció | Descripció |
| :--- | :--- |
| **Inici** | El jugador surt de **A0** amb **32 punts**. |
| **Moviment** | Cada pas a una casella segura indica la distància a la lava més propera. |
| **Lava** | Trepitjar lava resta **1 punt**. La casella queda marcada amb una `l`. |
| **Puntuació** | Pots consultar els punts restants i la distància al tresor en qualsevol moment. |
| **Derrota** | Si els punts arriben a **0** o si el jugador surt dels límits del tauler. |
| **Victòria** | Arribar a la casella **F7**. |

---

## 🚀 Instruccions d'Execució

1. Situa't a la carpeta del projecte des de la terminal.
2. Executa la comanda:
   ```bash
   npm start