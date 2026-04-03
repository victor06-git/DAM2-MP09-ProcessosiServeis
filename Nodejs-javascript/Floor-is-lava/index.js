const readline = require('readline');
const GameLoop = require('./gameLoop');

const game = new GameLoop();
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

function demanarComanda() {
    console.log(game.render());
    if (game.estat.trampaActiva) {
        console.log("--- TAULER TRAMPA ---");
        console.log(game.render(true));
    }

    rl.question("Escriu una comanda: ", (input) => {
        const parts = input.trim().split(" ");
        const cmd = parts[0].toLowerCase();
        const arg = parts[1];
        const fitxer = parts[2];

        switch (cmd) {
            case 'ajuda':
            case 'help':
                console.log("\n[ajuda, caminar, guardar partida, carregar partida, activar/desactivar trampa, puntuació]");
                break;

            case 'caminar':
                const resultat = game.moure(arg);
                console.log("\n" + resultat.msg);
                if (resultat.status === "win" || resultat.status === "lose") process.exit();
                break;

            case 'activar':
                if (arg === 'trampa') game.estat.trampaActiva = true;
                break;

            case 'desactivar':
                if (arg === 'trampa') game.estat.trampaActiva = false;
                break;

            case 'puntuació':
                const dist = Math.abs(game.estat.desti.r - game.estat.posicio.r) + Math.abs(game.estat.desti.c - game.estat.posicio.c);
                console.log(`\nPuntuació: Punts restants ${game.estat.punts}, Distància al destí: ${dist}`);
                break;

            case 'guardar':
                if (arg === 'partida') {
                    game.guardar(fitxer);
                    console.log("Partida guardada.");
                }
                break;

            case 'carregar':
                if (arg === 'partida') {
                    if (game.carregar(fitxer)) console.log("Partida carregada.");
                    else console.log("Error: No s'ha trobat l'arxiu.");
                }
                break;

            default:
                console.log("Comanda desconeguda.");
        }
        demanarComanda();
    });
}

console.log("--- BENVINGUT A FLOOR IS LAVA ---");
demanarComanda();