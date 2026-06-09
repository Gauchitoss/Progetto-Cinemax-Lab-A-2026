# 🎬 CineMax - Project Lab A 2026

Benvenuti nel repository ufficiale di **CineMax**, un sistema di gestione cinematografica basato su interfaccia testuale (TUI), sviluppato per il progetto di Laboratorio A 2026.

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

## Problemini
- verifica dati form che siano validi
  - anno di produzione maggiore rispetto a quello attuale
  - durata, eta consigliata, prezzo, posti in sala, anno di produzione1 non possono essere negativi
  - controllo che dati inseriti non siano troppo alti o irreali
- username to UpperCase
- modifica messaggio di errore dopo il login. perche in alcune schermate rimane quello, va fatto tornare default
- introduzione eccezzione in caso un pazzo compri un numeor di biglietti maggiore rispetto ai posti in sala
- introduzione possibilita' di modificare una propria prenotazione (rimuoverla)
- **Rendere impossibile prenotare un film di una data passata2**
- **form registrazione**
  - data di nascita deve essere valida, inserire stesso concetto di quelle ceh vengono richiueste nei form
- riduzione dei posti disponibili assente. ossia quando un utente prenota dei posti il numero non decresce
- **bigliettaio**: dopo aver fatto le proeiezioni del giiorno, quando si trona indietro si va a finiere nella schermata di accesso
  - Il bigliettaio non deve prenotarsi nelal lista delle prenotazioni, deve vedendere i biglietti e nella schermata di conferrma vedere solo l'importo che il cliente deve pagare
  - Il numero di posti liberi in sala deve diminuire


## Cose strane
- form
  - nome regista puo' contenere numeri

## Possibili migliorie
- Al posto di posti in sala creare un enumerativo di 20 sale, dove il proezionista puo' scergliere in che sala mandare il film in modo che non bisogni scrivere ogni volta il numero dei posti della sala
  - Questa scelta implica controllo che durante lo stessto giorno e orario, tenendo conto della durata del film, non si prenotino due sale contemporaneamente