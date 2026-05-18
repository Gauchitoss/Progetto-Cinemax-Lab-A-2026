# 🎬 CineMax - Project Lab A 2026

Benvenuti nel repository ufficiale di **CineMax**, un sistema di gestione cinematografica basato su interfaccia testuale (TUI), sviluppato per il progetto di Laboratorio A 2026.

---

## Ultime Modifiche

- **Centralizzazione dello Stack**: Implementato lo `stackRecord` globale nella classe `CineMax`. Questo ha permesso di ripulire le firme di tutti i metodi in `LogicaStatiManager`, eliminando il passaggio ridondante dei parametri.
- **Gestione delle Eccezioni**: Introdotti blocchi `try-catch` specifici per `NumberFormatException` e `ArrayIndexOutOfBoundsException`. Ora il sistema non crasha in caso di input errati, ma reindirizza l'utente a uno `STATO_ERRORE` dedicato.
- **Sistema di Navigazione Dinamico**: Il metodo `statoMenuSuccessivo` ora mappa automaticamente l'input numerico dell'utente agli stati definiti nell'Enum, rendendo l'aggiunta di nuovi menu estremamente rapida.
- **UI & UX Improvement**: 
  - Creazione di un nuovo logo **ERROR** simmetrico e stilizzato in ANSI Art per il feedback degli errori.
  - Implementazione del comando `:q` per annullare l'inserimento nei form e tornare indietro nello stack.
  - Ottimizzazione del sistema di coordinate ANSI (`\033[...]`) per l'input dinamico nei box.

---

## TODO

### 🔴 Priorità Alta

  - [x] **Refactoring del codice**, cercare di eliminare l schiere di switch case all'interno di LogicaStatiManager.
  Aggiungere la logica degli stati all'enumerativo MenuManager.
- [ ] **Correzione di bug e sistemazione errori**, aggiungere eccezzioni personalizzate
  - [ ] **Visualizza Proezioni**, terminare questa parte e mandare a schermo i film.

### 🟡 Priorità Media

- [ ] **Gestione Prenotazioni**: Sviluppare la logica per permettere ai clienti di selezionare i posti e salvare la prenotazione.
- [ ] **Filtri di Ricerca**: Completare la logica nel metodo `gestisciCercaFlm` per filtrare la lista proiezioni per titolo, genere o prezzo.
- [ ] **Stato Sala**: Implementare la visualizzazione grafica dei posti occupati/liberi per il Bigliettaio.

### 🟢 Ottimizzazioni

- [ ] Creare un set di eccezioni personalizzate (es. `InvalidLoginException`, `DataFormatException`) per una gestione ancora più granulare.
- [ ] Differenziare graficamente i messaggi tra "Errore di Formato" (input non numerico) e "Errore di Scelta" (numero fuori range).

---

## Team

| Nome | Matricola | Sede |
| :--- | :--- | :--- |
| **Baroncelli Luca** | 761582 | VA |
| **Modena Matteo** | 765099 | VA |
| **Bin Alessio** | 762387 | VA |

---

## 📐 Note Tecniche

Il progetto utilizza un'architettura **State Machine** basata su uno **Stack LIFO** (Last-In-First-Out). Ogni azione dell'utente può effettuare un `push` (avanzamento), un `pop` (ritorno) o un `clear` (reset/logout) dello stato applicativo.