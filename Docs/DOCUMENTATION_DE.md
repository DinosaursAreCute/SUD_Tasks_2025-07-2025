# SUD_Tasks: Detaillierte Funktionsdokumentation

Dieses Dokument bietet einen umfassenden Überblick über alle Klassen und Funktionen im SUD_Tasks-Repository.

---

## Inhaltsverzeichnis
- [Mitarbeiter-Modul](#mitarbeiter-modul)
- [Formen-Modul](#formen-modul)
- [Fahrzeug-Modul](#fahrzeug-modul)
- [Testklassen](#testklassen)
- [Allgemeine Hinweise](#allgemeine-hinweise)

---

## Mitarbeiter-Modul

### Department.java
- **Department(String name, Manager head)**: Konstruktor. Erstellt eine Abteilung mit einem Namen und einem Manager als Leiter.
- **void addEmployee(Employee e)**: Fügt einen Mitarbeiter zur Abteilung hinzu.
- **boolean removeEmployee(Employee e)**: Entfernt einen Mitarbeiter aus der Abteilung.
- **Manager changeHead(Manager newHead)**: Ändert den Abteilungsleiter zu einem neuen Manager.

### Driver.java
- **Driver(int id, String name, double hourlyRate, char license)**: Konstruktor. Erstellt einen Fahrer mit einer Lizenz.
- **void setLicense(char license)**: Legt den Lizenztyp des Fahrers fest.
- **DriverLicense getLicense()**: Gibt den Lizenztyp des Fahrers zurück.

### Employee.java
- **Employee(int id, String name)**: Abstrakte Basisklasse für Mitarbeiter.
- **void setId(int id)**: Legt die ID des Mitarbeiters fest.
- **void setName(String name)**: Legt den Namen des Mitarbeiters fest.

### EmployeeManagement.java
- **void addEmployee(Employee e)**: Fügt einen Mitarbeiter zum Verwaltungssystem hinzu.
- **boolean removeEmployee(Employee e)**: Entfernt einen Mitarbeiter aus dem Verwaltungssystem.

### Manager.java
- **Manager(int id, String name, double fixedSalary, double bonusPercent)**: Konstruktor. Erstellt einen Manager mit einem festen Gehalt und einem Bonusprozentsatz.
- **double getBonus(double base)**: Berechnet den Bonus basierend auf einem Grundgehalt.

### OfficeWorker.java
- **OfficeWorker(int id, String name, double fixedSalary)**: Konstruktor. Erstellt einen Büroangestellten mit einem festen Gehalt.
- **double getSalary()**: Gibt das feste Gehalt zurück.

### ShiftWorker.java
- **ShiftWorker(int id, String name, double hourlyRate)**: Konstruktor. Erstellt einen Schichtarbeiter mit einem Stundenlohn.
- **void work()**: Erhöht die gearbeiteten Stunden um 8.
- **void work(int hours)**: Erhöht die gearbeiteten Stunden um einen angegebenen Betrag.

---

## Formen-Modul

### Circle.java
- **Circle(double radius)**: Konstruktor. Erstellt einen Kreis mit einem Radius.
- **double getArea()**: Gibt die Fläche des Kreises zurück.
- **double getPerimeter()**: Gibt den Umfang des Kreises zurück.

### Cone.java
- **Cone(double radius, double height)**: Konstruktor. Erstellt einen Kegel mit einer kreisförmigen Basis und einer Höhe.
- **double getVolume()**: Gibt das Volumen des Kegels zurück.

### Cuboid.java
- **Cuboid(double width, double height, double depth)**: Konstruktor. Erstellt einen Quader mit Breite, Höhe und Tiefe.
- **double getVolume()**: Gibt das Volumen des Quaders zurück.

### Cylinder.java
- **Cylinder(double radius, double height)**: Konstruktor. Erstellt einen Zylinder mit einer kreisförmigen Basis und einer Höhe.
- **double getVolume()**: Gibt das Volumen des Zylinders zurück.

### Rectangle.java
- **Rectangle(double width, double height)**: Konstruktor. Erstellt ein Rechteck mit Breite und Höhe.
- **double getArea()**: Gibt die Fläche des Rechtecks zurück.

### RegularPrism.java
- **RegularPrism(int nSides, double sideLength, double height)**: Konstruktor. Erstellt ein regelmäßiges Prisma mit einer polygonalen Basis.
- **double getVolume()**: Gibt das Volumen des Prismas zurück.

### RegularPyramid.java
- **RegularPyramid(int nSides, double sideLength, double height)**: Konstruktor. Erstellt eine regelmäßige Pyramide mit einer polygonalen Basis.
- **double getVolume()**: Gibt das Volumen der Pyramide zurück.

### Sphere.java
- **Sphere(double radius)**: Konstruktor. Erstellt eine Kugel mit einem Radius.
- **double getVolume()**: Gibt das Volumen der Kugel zurück.

---

## Fahrzeuge-Modul

### Bus.java
- **Bus(double tankSize, double maxSpeed, GPSPosition position, int seatCount, String driverName)**: Konstruktor. Erstellt einen Bus mit einem Fahrernamen.
- **void setDriver(Driver driver)**: Legt den Fahrer für den Bus fest.

### FleetManagement.java
- **void addVehicle(Vehicle v)**: Fügt ein Fahrzeug zur Flotte hinzu.
- **boolean removeVehicle(Vehicle v)**: Entfernt ein Fahrzeug aus der Flotte.

### GPSPosition.java
- **GPSPosition(double longitude, double latitude)**: Konstruktor. Erstellt eine GPS-Position mit Längengrad und Breitengrad.
- **void setPosition(double longitude, double latitude)**: Legt die GPS-Position fest.

### PassengerVehicle.java
- **PassengerVehicle(double tankSize, double maxSpeed, GPSPosition position, int seatCount)**: Konstruktor. Erstellt ein Passagierfahrzeug mit Sitzanzahl.

### Truck.java
- **Truck(double tankSize, double maxSpeed, GPSPosition position, double loadingArea)**: Konstruktor. Erstellt einen LKW mit einer Ladefläche.
- **void load(double area)**: Lädt Fracht in den LKW.

### Vehicle.java
- **Vehicle(double tankSize, double maxSpeed, GPSPosition position)**: Abstrakte Basisklasse für Fahrzeuge.
- **void refuel(double amount)**: Betankt das Fahrzeug.

---

## Testklassen

### TestDepartment.java
- **void main(String[] args)**: Testet die Department-Klasse.

### TestEmployee.java
- **void main(String[] args)**: Testet die Vererbungshierarchie der Mitarbeiter.

### TestShapes.java
- **void main(String[] args)**: Testet das Formen-Modul.

### TestVehicle.java
- **void main(String[] args)**: Testet das Fahrzeuge-Modul.

---

## Allgemeine Hinweise
- Alle Klassen folgen den Standard-Java-Konventionen und sind thematisch organisiert.
- Abstrakte Klassen und Schnittstellen werden für Erweiterbarkeit und CompanyManagement-Wiederverwendung verwendet.
- Testklassen bieten einfache Demonstrationen und können direkt ausgeführt werden.

---

Wenn du mehr Details zu einer bestimmten Klasse oder Funktion benötigst, sieh dir den Quellcode an oder fordere eine detaillierte Erklärung an.
