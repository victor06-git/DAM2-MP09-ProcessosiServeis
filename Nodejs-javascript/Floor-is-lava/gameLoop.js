const fs = require('fs');

class GameLoop {
    constructor() {
        this.FILES = 6;
        this.COLUMNES = 8;
        this.LAVA_COUNT = 16;
        this.FILES_LABELS = ['A', 'B', 'C', 'D', 'E', 'F'];
        this.reset();
    }

    reset() {
        this.estat = {
            posicio: { r: 0, c: 0 },
            desti: { r: 5, c: 7 },
            punts: 32,
            lava: [],
            trampaActiva: false
        };
        this.generarLava();
    }

    generarLava() {
        while (this.estat.lava.length < this.LAVA_COUNT) {
            let r = Math.floor(Math.random() * this.FILES);
            let c = Math.floor(Math.random() * this.COLUMNES);
            const esInici = (r === 0 && c === 0);
            const esDesti = (r === 5 && c === 7);
            const jaHiEs = this.estat.lava.some(l => l.r === r && l.c === c);

            if (!esInici && !esDesti && !jaHiEs) {
                this.estat.lava.push({ r, c, trepitjada: false });
            }
        }
    }

    getDistanciaLava() {
        let distancies = this.estat.lava.map(l => 
            Math.abs(l.r - this.estat.posicio.r) + Math.abs(l.c - this.estat.posicio.c)
        );
        return Math.min(...distancies);
    }

    moure(direccio) {
        let novaPos = { ...this.estat.posicio };
        if (direccio === 'amunt') novaPos.r--;
        else if (direccio === 'avall') novaPos.r++;
        else if (direccio === 'esquerra') novaPos.c--;
        else if (direccio === 'dreta') novaPos.c++;
        else return { msg: "Direcció no vàlida.", status: "error" };

        if (novaPos.r < 0 || novaPos.r >= this.FILES || novaPos.c < 0 || novaPos.c >= this.COLUMNES) {
            return { msg: "Has perdut, has caigut per un penyasegat", status: "lose" };
        }

        this.estat.posicio = novaPos;
        const lavaAqui = this.estat.lava.find(l => l.r === novaPos.r && l.c === novaPos.c);

        if (lavaAqui) {
            lavaAqui.trepitjada = true;
            this.estat.punts--;
            if (this.estat.punts <= 0) return { msg: "Has perdut, ja no tens més passes", status: "lose" };
            return { msg: "Has trepitjat lava, perds un punt", status: "lava" };
        }

        if (novaPos.r === this.estat.desti.r && novaPos.c === this.estat.desti.c) {
            return { msg: `Has guanyat, has arribat al final amb ${this.estat.punts} punts`, status: "win" };
        }

        return { msg: `Vas per bon camí, tens lava a ${this.getDistanciaLava()} caselles de distància`, status: "ok" };
    }

    render(revelar = false) {
        let output = "\n  01234567\n";
        for (let r = 0; r < this.FILES; r++) {
            let fila = this.FILES_LABELS[r];
            for (let c = 0; c < this.COLUMNES; c++) {
                const esJugador = this.estat.posicio.r === r && this.estat.posicio.c === c;
                const esDesti = this.estat.desti.r === r && this.estat.desti.c === c;
                const lava = this.estat.lava.find(l => l.r === r && l.c === c);

                if (esJugador) fila += "T";
                else if (esDesti) fila += "*";
                else if (lava && (revelar || lava.trepitjada)) fila += "l";
                else fila += "·";
            }
            output += fila + "\n";
        }
        return output;
    }

    guardar(nom) {
        fs.writeFileSync(nom, JSON.stringify(this.estat));
    }

    carregar(nom) {
        if (fs.existsSync(nom)) {
            this.estat = JSON.parse(fs.readFileSync(nom));
            return true;
        }
        return false;
    }
}

module.exports = GameLoop;