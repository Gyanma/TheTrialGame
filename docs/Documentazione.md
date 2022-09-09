# The Trial

## Autore: 
Gianmarco Rutigliano, matricola 747002

## Indice:

    Sez. 1. L'avventura testuale
        - Descrizione del gioco
        - Struttura delle mappe
        - Regole e obiettivi del gioco
        - Possibili comandi
        - Soluzioni agli indovinelli
    Sez. 2.	Descrizione tecnica
        - Architettura del sistema
        - Diagramma delle classi
        - Specifica algebrica
        - Elementi implementati


## Sezione 1:  L'avventura testuale

> ### Descrizione del gioco

"The trial" è un’avventura testuale con elementi di RPG, roguelike e puzzle games.

L’ambientazione si ispira ai giochi “World of Warcraft” e “Slay the Spire”, mentre gli indovinelli che si possono trovare durante una sessione di gioco sono ispirati a puzzle presentati in diverse installazioni della serie di giochi “Il professor Layton”. Il gioco presenta inoltre una modalità di combattimento a turni.

L’anonimo protagonista è un giovane combattente che ha da poco terminato il suo addestramento. Per accedere al rango di Paladino di Roccavento, deve superare tre labirinti e sconfiggere i tre guardiani che si trovano al loro interno.

Ma non è tutto. La prova è strutturata per testare l’arguzia, l’inventiva e la bontà d’animo del giovane. Per questa ragione, all’interno del labirinto sono state posizionate trappole, da cui lo sfidante potrà liberarsi solo se avrà raccolto gli strumenti giusti, e marchingegni che bloccheranno il passaggio e richiederanno di inserire un codice; inoltre, il labirinto è popolato da creature di ogni tipo: alcune saranno aggressive e dovranno essere sconfitte in combattimento, mentre altre necessiteranno di aiuto per uscire da situazioni spinose. Starà al protagonista risolvere i problemi che incontrerà e raggiungere indenne l’uscita.
  
> ### Struttura delle mappe

Il gioco non presenta una mappa unica. Ogni sessione di gioco genera casualmente tre mappe, una per ogni labirinto, con un numero di stanze che varia da 9 a 16. Ogni labirinto presenta sempre un numero di oggetti e di eventi sparpagliati per le sue stanze calcolati in misura alla grandezza della mappa.

> Mappe d'esempio

![Map1](/docs/Map1.png)
![Map2](/docs/Map2.png)
![Map3](/docs/Map3.png)

Le mappe sono orientate lungo i sensi cardinali: dunque il giocatore può spostarsi tra le stanze in direzioni nord, sud, est o ovest, se esiste effettivamente un passaggio verso una stanza in quella specifica direzione.

Il giocatore ha a disposizione sin da subito la possibilità di stampare una mappa del labirinto in cui si trova al momento, dove è indicata la stanza di partenza. Inoltre, tramite un incontro specifico, è possibile ottenere una versione migliorata della mappa che segnala la stanza dove il giocatore si trova al momento e la stanza dove si trova l’uscita del labirinto.

> ### Regole e obiettivo del gioco

Per procedere nel gioco, il giocatore può esplorare ogni stanza per cercare oggetti presenti nella stanza o individuare quale sia l’evento caratterizzante della stanza. Sebbene non sia strettamente necessario al completamento del gioco (eccezion fatta per delle categorie specifiche), interagire con gli eventi permette di ottenere un punteggio maggiore alla conclusione del gioco.
Ogni oggetto può essere preso, ma non è possibile trasportare due oggetti uguali. Alcuni oggetti possono essere utilizzati per ottenere effetti permanenti, mentre altri hanno una durata specifica. Gli oggetti possono inoltre essere esaminati, scambiati o gettati. Di importanza fondamentale nel gioco sono le armi, gli oggetti principali con cui combattere i nemici.
Gli eventi si dividono in 4 categorie:
-	*Indovinelli*: questi eventi consistono in un quesito o una prova posta da un NPC. Non sono fondamentali per il completamento della storia, ma permettono di ottenere delle ricompense e di acquisire un punteggio maggiore. Eccezione sono i meccanismi, ossia indovinelli che bloccano l’accesso ad altre stanze e richiedono di comporre un codice seguendo le regole dell’indovinello.
-	*Nemici*: questi eventi consistono in una creatura che affronta il giocatore in un combattimento a turni. È tecnicamente possibile affrontare solo il boss in caso si trovi la stanza finale prima di incontrare stanze con nemici, ma è impossibile uscire da una stanza con un nemico senza prima averlo sconfitto. Sconfiggere un nemico o un boss aumenta i punti vita del proprio personaggio.
-	*Trappole*: questi eventi bloccano il giocatore in una corsa contro il tempo. C’è un pericolo imminente che richiede di essere affrontato in un tempo limitato, con una penalità in caso il giocatore non abbia l’oggetto adatto o non riesca a trovarlo in tempo. Finché il giocatore è in trappola non può effettuare la maggior parte delle azioni non inerenti alla ricerca di una soluzione, incluso uscire dalla stanza.
-	*Incontri pacifici*: sono incontri in cui il giocatore ha la possibilità di scambiare degli oggetti che ha nel suo inventario per altri oggetti, oppure di ricevere dei potenziamenti. Anche questi incontri non sono fondamentali per raggiungere la fine del gioco.

> ### Comandi
Per ogni comando sono previsti una serie di comandi alternativi di significato simile. Di seguito sono elencate le azioni possibili e degli esempi di comandi che le eseguono:
-	Individuare gli eventi

        Esplora la stanza
        Perlustra la stanza

-	Cercare oggetti

    	Cerca degli oggetti
        Cerca degli item

-	Muoversi tra le stanze
    
    	Spostati verso nord
    	Cammina verso ovest

-	Raccogliere un oggetto
    
    	Raccogli l’ascia
        Prendi la fune

-	Aprire la mappa
    
    	Apri la mappa
        Guarda la mappa

-	Avviare un evento
    
    	Parla con il goblin
    	Raggiungi la fontana

-	Tentare di sbloccare un meccanismo
    
    	Esamina il meccanismo
        Sblocca il meccanismo

-	Raggiungi la mappa successiva
    	
        Esci dal labirinto
    	Raggiungi l’uscita

-	Aprire l’inventario
    
    	Apri l’inventario
    	Vedi l’inventario

-	Utilizzare un item
    	
        Usa la vanga
    	Utilizza l’affilatore sulla spada

-	Esaminare un item nell’inventario
    
    	Esamina il cibo
    	Studia l’arco

-   Scambiare un item con un personaggio

        Consegna l'acchiappasogni
        Offri il pugnale per la pozione del fuoco

Si aggiunge il comando unico "Chiudi il gioco" che permette di chiudere l'applicazione.

> ### Soluzioni

**Indovinelli**

    -	Avventuriero: 421
    -	Pirati: 9
    -	Goblin: Quadri. Jolly, Cuori, Fiori, Picche
    -	Orco: 4
    -	Centauri: Terzogenito
    -	Nani: 4
    -	Folletto: Forziere di ferro
    -	Cavaliere: Il cavaliere
    -	Soldato: 26
    -	Elfo: 12
    -	Grifone: 4


**Indovinelli con le brocche**

Brocche di acqua:

	1° Brocca nella 2° Brocca (3, 5, 0)
	2° Brocca nella 3° Brocca (3, 2, 3)
	3° Brocca nella 1° Brocca (6, 2, 0)
	2° Brocca nella 3° Brocca (6, 0, 2)
	1° Brocca nella 2° Brocca (1, 5, 2)
	2° Brocca nella 3° Brocca (1, 4, 3)
	3° Brocca nella 1° Brocca (4, 4, 0)

Brocche di latte:

	1° Brocca nella 2° Brocca (3, 7, 0)
	2° Brocca nella 3° Brocca (3, 4, 3)
	3° Brocca nella 1° Brocca (6, 4, 0)
	2° Brocca nella 3° Brocca (6, 1, 3)
	3° Brocca nella 1° Brocca (9, 1, 0)
	2° Brocca nella 3° Brocca (9, 0, 1)
	1° Brocca nella 2° Brocca (2, 7, 1)
	2° Brocca nella 3° Brocca (2, 5, 3)
	3° Brocca nella 1° Brocca (5, 5, 0)

Brocche di vino:

	1° Brocca nella 2° Brocca (7, 9, 0)
	2° Brocca nella 3° Brocca (7, 2, 7)
	3° Brocca nella 1° Brocca (14, 2, 0)
	2° Brocca nella 3° Brocca (14, 0, 2)
	1° Brocca nella 2° Brocca (5, 9, 2)
	2° Brocca nella 3° Brocca (5, 4, 7)
	3° Brocca nella 1° Brocca (12, 4, 0)
	2° Brocca nella 3° Brocca (12, 0, 4)
	1° Brocca nella 2° Brocca (3, 9, 4)
	2° Brocca nella 3° Brocca (2, 6, 7)
	3° Brocca nella 1° Brocca (10, 6, 0)
	2° Brocca nella 3° Brocca (10, 0, 6)
	1° Brocca nella 2° Brocca (1, 9, 6)
	2° Brocca nella 3° Brocca (1, 8, 7)
	3° Brocca nella 1° Brocca (8, 8, 0)

**Pericoli:**

    -	Animali: Usa il cibo
    -	Incendio: Usa l'acqua
    -	Spettri: Usa la torcia
    -	Stanza allagata: Usa la fune

**Meccanismi**
	
    Gli zeri:
    Premere i tasti in questa sequenza: D quattro volte, B nove volte, A una volta, C sette volte


    Combinazione Criptica:
    Attivare tutti i tasti, eccetto 4, 5, 11, 12.


    33333:
    Una possibile soluzione è la seguente: 
    41268-
    7935


___

## Sez. 2.	Descrizione tecnica
   
>### Architettura del sistema

L'applicazione, scritta interamente in java, è suddivisa in più sezioni per migliorare la leggibilità e la gestibilità

Il package "**operations**" contiene classi che non conservano dati, ma presentano metodi che permettono di modificare i dati. Invece, le classi contenute nel package "**resources**" sono classi che conservano dati sugli elementi di gioco.

> Classi Operations

**Package "actions"**

    A questo package sono destinate le classi che modificano i dati di gioco in risposta ai comandi inseriti dal giocatore, più la classe Interpreter che ha il ruolo di individuare l'azione corretta in base al comando ricevuto. 

**Package "inputoutput"**

    A questo package sono destinate le classi il cui scopo è gestire input e output da terminale particolarmente complessi. Al momento esse consistono nella classe Parser, che si occupa di analizzare i comandi e verificarne sintassi e vocabolario per verificarne la validità, e la classe Printer che si occupa di formattare gli output su terminale e stampare testi da file.

**Package "runHandler"**

    A questo package sono destinate le classi che determinano l'inizializzazione di una run (importando i dati di gioco e generando le mappe della sessione) e ne gestiscono lo svolgimento.

**Package "visualHandler"**

    A questo package sono destinate le classi che gestiscono le interfacce grafiche. Queste al momento si limitano a elementi di gioco quali le mappe o i meccanismi. Il package contiene anche delle classi che gestiscono una queue: questa è strutturata in modo da impedire al giocatore di utilizzare il terminale durante la risoluzione dei meccanismi.

**Package "utilities"**

    A questo package sono destinate le classi con usi generici. In particolare al momento esso contiene la classe Utilities, che contiene metodi che svolgono operazioni di controllo complesse o utili in più punti del codice.

> Classi Resources

**Directory "archive"**

    Questa directory contiene i dati di gioco predefiniti: dati degli eventi, degli item, dei testi e dei vocabolari che vengono utilizzati nel gioco.

**Package "events" e "items"**

    Queste directory contengono classi archetipo di dati di gioco.


>### Diagramma delle classi

>### Specifica algebrica

>### Elementi implementati

**Programmazione ad oggetti**

    
