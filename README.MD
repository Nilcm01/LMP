# Llibreria Musical Portàtil

La meva aplicació de gestió i sincronització de música amb un dispositiu extern.

## Descripció

Des que em vaig fer un iPod 5.5G d'alta capacitat, he volgut tenir una eina que em permeti gestionar la música que hi poso de forma més eficient, ja que copiar i enganxar manualment els directoris no és la millor manera. Descartant altres mètodes com iTunes (no em serveix, ja que ara uso Rockbox), he fet la meva pròpia aplicació (avantatges d'estudiar el que estudio).

## Funcionalitats

- ***Adaptabilitat d'adreces***: Escull la unitat de disc on tens la música i la unitat de disc del dispositiu extern.
- ***Consulta la llibreria***: Visualitza i navega pels diferents artistes i àlbums de la llibreria.
- ***Diferents formats***: Un mateix àlbum pot tenir diferents formats (MP3, FLAC, etc.). La llibreria els agrupa tots junts dins el mateix índex, però permetent-ne sincronitzar-ne la versió que vulguis.
- ***Identifica què està sincronitzat***: L'estat de sincronització de cada àlbum es mostra amb un simple codi de colors.
- ***Envia àlbums***: Selecciona els àlbums que vols sincronitzar i fes-ho amb un sol clic.
- ***Elimina'ls***: Si ja no vols un àlbum al dispositiu, pots eliminar-lo amb un sol clic.

## Tasques pendents

- ***Desat de configuració***: Actualment, les adreces de les unitats de disc s'han de configurar cada cop que s'obre l'aplicació. Caldria que es desessin per evitar-ho.
- ***Enviament ràpid***: Sense necessitat de seleccionar la variant, enviar l'àlbum amb un sol clic prenent com a opció per defecte FLAC/16-44 (o la més propera).
- ***Eliminació ràpida***: Amb un sol clic, eliminar l'àlbum seleccionat sense haver d'indicar variant o eliminar tots els àlbums d'un artista.

## Avís sobre l'ús

Aquesta aplicació és un projecte personal, per tant, no està pensada per a un ús general i és molt possible que no funcioni adequadament en el vostre cas particular: està feta tenint únicament en compte les meves necessitats i gustos.

Si us agrada i voldríeu fer-la servir, us animo a crear-ne la vostra pròpia versió a partir d'aquesta. Això sí, sempre tenint en compte els límits de la llicència.

## A nivell tècnic

- ***Java 23.0.1***: El core i lògica de l'aplicació.
- ***Java Swing***: La interfície gràfica.

## Construcció

Es recomana fer servir ***IntelliJ IDEA*** per a la construcció de l'aplicació, plataforma per la qual s'hi inclouen els fitxers de configuració. L'únic requisit és tenir instal·lat ***Java 23 (openjdk-23)*** o superior.

1. Construïu el projecte fent servir la configuració "Main".
2. Construïu el JAR fent servir la configuració de creació d'artefactes "LMP.jar".
3. El JAR resultant es trobarà a la carpeta ***~/out***.

## Crèdits

- ***Autor***: [Nilcm01](https://github.com/Nilcm01)

## Llicència

[LMP](https://github.com/Nilcm01/LMP) © 2024 de [Nilcm01](https://github.com/Nilcm01) té llicència [CC BY-NC-SA 4](https://creativecommons.org/licenses/by-nc-sa/4.0).

### Sou lliure de:
- Compartir: copiar i redistribuir el material en qualsevol mitjà i format
- Adaptar: remesclar, transformar i crear a partir del material 

El llicenciador no pot revocar aquestes llibertats, sempre que seguiu els termes de la llicència.

### Amb els termes següents:
- Reconeixement: Heu de reconèixer l'autoria de manera apropiada , proporcionar un enllaç a la llicència i indicar si heu fet algun canvi . Podeu fer-ho de qualsevol manera raonable, però no d'una manera que suggereixi que el llicenciador us dóna suport o patrocina l'ús que en feu.
- NoComercial: No podeu utilitzar el material per a finalitats comercials .
- CompartirIgual: Si remescleu, transformeu o creeu a partir del material, heu de difondre les vostres creacions amb la mateixa llicència que l'obra original.

No hi ha cap restricció addicional: No podeu aplicar termes legals ni mesures tecnològiques que restringeixin legalment a altres de fer qualsevol cosa que la llicència permet.

### Avisos:
- No heu de complir amb la llicència per als elements del material en el domini públic o quan el seu ús està permès per l'aplicació d'una excepció o limitació dels drets d'autor.
- No es donen garanties. La llicència pot no ser suficient per autoritzar la utilització que en voleu fer. Per exemple, altres aspectes com la publicitat, la privacitat, o els drets morals poden limitar la forma d'utilitzar el material.