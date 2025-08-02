# SUD_Tasks: Schulprogrammieraufgaben

Willkommen im **SUD_Tasks**-Repository! Dieses Projekt enthält meine Lösungen zu verschiedenen Programmieraufgaben, die in unseren Schulstunden gestellt wurden. Der CompanyManagement ist thematisch organisiert und demonstriert grundlegende objektorientierte Programmierkonzepte in Java.

## Projektstruktur

- `CompanyManagement/Employees/` — Klassen und Tests im Zusammenhang mit der Mitarbeiterverwaltung (z. B. `Employee`, `Manager`, `Department` usw.)
- `CompanyManagement/Shapes/` — Klassen für 2D- und 3D-Geometrieformen und deren Eigenschaften
- `CompanyManagement/Vehicles/` — Klassen zur Fahrzeugverwaltung (z. B. `Vehicle`, `Bus`, `Truck` usw.)
- `CompanyManagement/Test/` — Testklassen für die Hauptmodule
- `Feature_Files/` — Aufgabenbeschreibungen und unterstützende Dokumente

## Java-Versionen
- **Verwendete Version:** Java 17
- **Niedrigste unterstützte Version:** Java 8

## Ausführung
1. Klone das Repository:
   ```sh
   git clone https://github.com/DinosaursAreCute/SUD_Tasks.git
   ```
2. Öffne das Projekt in deiner bevorzugten IDE (z. B. VS CompanyManagement, IntelliJ IDEA, Eclipse).
3. Kompiliere die Java-Dateien. Zum Beispiel aus dem `CompanyManagement`-Verzeichnis:
   ```sh
   javac Employees/*.java Shapes/*.java Vehicles/*.java Test/*.java
   ```
4. Führe eine beliebige Testklasse aus, z. B.:
   ```sh
   java Employees.TestDepartment
   ```

## FAQ
- **F: Kann ich diesen CompanyManagement für meine eigenen Aufgaben verwenden?**
  - Ja, aber bitte nutze ihn nur als Referenz und reiche ihn nicht unverändert ein. Passe ihn an deine Bedürfnisse an.

## Beitrag leisten
Beiträge sind willkommen! So kannst du beitragen:
1. Forke das Repository.
2. Erstelle einen neuen Branch für deine Funktion oder Fehlerbehebung:
   ```sh
   git checkout -b addition-name
   ```
3. Committe deine Änderungen mit einer klaren Nachricht:
   ```sh
   git commit -m "Funktion hinzugefügt"
   ```
4. Pushe deinen Branch:
   ```sh
   git push origin addition-name
   ```
5. Öffne eine Pull-Anfrage und beschreibe deine Änderungen.

Falls du weitere Informationen benötigst, lies die [CONTRIBUTING_DE.md](Docs/CONTRIBUTING_DE.md).

### Mit VS CompanyManagement beitragen

Wenn du Visual Studio CompanyManagement verwenden möchtest, um beizutragen, folge diesen Schritten:

1. **Repository klonen**:
   - Öffne das Terminal in VS CompanyManagement und führe aus:
     ```sh
     git clone https://github.com/DinosaursAreCute/SUD_Tasks.git
     ```

2. **Projekt öffnen**:
   - Navigiere zum geklonten Ordner und öffne ihn in VS CompanyManagement.

3. **Neuen Branch erstellen**:
   - Öffne das Terminal in VS CompanyManagement und führe aus:
     ```sh
     git checkout -b addition-name
     ```

4. **Änderungen vornehmen**:
   - Nutze den VS CompanyManagement-Editor, um Dateien zu bearbeiten oder neue hinzuzufügen.

5. **Änderungen stagen und committen**:
   - Nutze das Quellcode-Kontrollpanel in VS CompanyManagement, um deine Änderungen zu stagen.
   - Füge eine Commit-Nachricht hinzu und klicke auf das Häkchen, um zu committen.

6. **Änderungen pushen**:
   - Öffne das Terminal und führe aus:
     ```sh
     git push origin addition-name
     ```

7. **Pull-Anfrage erstellen**:
   - Gehe zum GitHub-Repository und öffne eine Pull-Anfrage für deinen Branch.

### Legende der GitHub-Begriffe

- **Repository (Repo):** Ein Speicherplatz, in dem dein Projekt gespeichert ist. Es enthält alle deine Projektdateien und deren Verlauf.
- **Branch:** Eine separate Version deines Codes, in der du Änderungen vornehmen kannst, ohne den Hauptcode zu beeinflussen.
- **Commit:** Ein Schnappschuss deiner Änderungen. Jeder Commit hat eine eindeutige ID und eine Nachricht, die die Änderungen beschreibt.
- **Pull-Anfrage (PR):** Eine Anfrage, um deine Änderungen von einem Branch in einen anderen (normalerweise den Hauptbranch) zu übernehmen.
- **Fork:** Eine Kopie eines Repositories, die du ändern kannst, ohne das Originalprojekt zu beeinflussen.
- **Klonen:** Eine lokale Kopie eines Repositories auf deinem Computer.
- **Merge:** Das Zusammenführen von Änderungen von einem Branch in einen anderen.
- **Stagen:** Änderungen vorbereiten, die in einen Commit aufgenommen werden sollen.
- **Push:** Deine committen Änderungen an ein Remote-Repository (z. B. GitHub) senden.
- **Pull:** Änderungen von einem Remote-Repository in dein lokales Repository abrufen und zusammenführen.

## Mitwirkende
- **Repo-Besitzer:** [DinosaursAreCute](https://github.com/DinosaursAreCute)
