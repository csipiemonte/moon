Come funziona il tema moonfo_theme?

Il file moonfo_theme fa un import di tutti i file .scss (Sass) che servono a compilare il file .css finale.
Vengono importati i file di Bootstrap e i file di Bootstrap Italia, presenti nei node_modules.


Come apportare modifiche ai file di Bootstrap Italia (es. variabili di colore)?

1. Individuare il file che contiene il codice da modificare (es. 'colors_vars.scss')
2. copiare il file nella directory principale del tema e rinominarlo aggiungendo '_custom' alla fine del nome del file
3. importare il file appena creato nel file 'moonfo_theme.scss' nella riga APPENA PRECEDENTE alla riga di import del file originale, ad esempio:


@import '/src/moonfo_theme/scss/colors_vars_custom';
@import '/src/moonfo_theme/bootstrap-italia/scss/utilities/colors_vars';

!!! ATTENZIONE: il file variables.scss di Bootstrap Italia viene sostituito da variables_custom, altrimenti le variabili non vengono sovrascritte correttamente.