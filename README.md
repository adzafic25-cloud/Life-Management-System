Life Management System je modularna desktop aplikacija razvijena u programskom jeziku Java, oslonjena na NoSQL arhitekturu baze podataka (MongoDB). Sistem je dizajniran da služi kao personalizovani asistent za upravljanje ključnim aspektima svakodnevnog života.

1. Arhitektura baze podataka i povezivanje
MongoDBConnection
Ova klasa predstavlja infrastrukturni temelj aplikacije. Implementira logiku povezivanja na MongoDB klijent putem Connection Stringa na lokalnom hostu (localhost:27017). Njena primarna uloga je menadžment resursa baze podataka, inicijalizacija baze LifeManagementDB i omogućavanje pristupa kolekcijama kao što su users, transactions i logs. Centralizacijom konekcije postignuta je stabilnost sistema i olakšano održavanje koda.

2. Autentifikacija i korisnički profili
LoginForm i RegisterForm
Ovi moduli čine sigurnosni sloj aplikacije.

RegisterForm vrši validaciju unosa i perzistenciju novih korisničkih dokumenata u bazu podataka, kreirajući jedinstvene identifikatore za svakog korisnika.

LoginForm implementira logiku pretrage baze podataka radi verifikacije kredencijala. Uspješna autentifikacija generiše sesiju koja omogućava personalizovan prikaz podataka unutar ostalih modula.

ProfileUpdateForm
Klasa zadužena za ažuriranje postojećih korisničkih podataka. Implementira Update operaciju nad dokumentima u kolekciji, omogućavajući dinamičku promjenu korisničkih preferenci i ličnih informacija bez narušavanja integriteta podataka.

3. Analiza funkcionalnih modula (Trackeri)
FinanceTrackerForm i Transaction
Ovaj modul je dizajniran za upravljanje ličnim kapitalom.

Klasa Transaction: Služi kao model podataka (POJO - Plain Old Java Object) koji enkapsulira atribute svake transakcije: iznos, tip (prihod/rashod), kategoriju i vremenski pečat.

FinanceTrackerForm: Implementira korisnički interfejs sa tabelarnim prikazom (JTable). Logika klase obuhvata agregaciju podataka iz baze radi izračunavanja ukupnog salda i filtriranja transakcija po datumima ili kategorijama.

HabitTrackerForm
Fokusira se na praćenje dnevnih ciljeva, sa posebnim naglaskom na hidrataciju.

Tehnička specifikacija: Klasa koristi JProgressBar komponentu za vizualizaciju progresa. Implementirana je logika inkrementalnog dodavanja unosa (čaša vode) gdje sistem u realnom vremenu komunicira sa bazom podataka, ažurira progres i dinamički mijenja vizuelne atribute interfejsa (npr. promjena boje pri ispunjenju dnevne kvote).

SleepTrackerForm
Modul za monitoring cirkadijalnog ritma i kvaliteta odmora.

Funkcionalnost: Klasa omogućava korisniku unos vremenskih intervala spavanja. Unutar klase je implementirana logika za izračunavanje delta vremena (razlika između vremena odlaska u krevet i vremena buđenja), uz dodavanje kvalitativnih parametara (ocjena sna) koji se čuvaju kao strukturirani podaci za kasniju analitičku obradu.

StudyPlannerForm
Alat za akademski menadžment i organizaciju učenja.

Opis: Omogućava kreiranje i upravljanje listom predmeta i zadataka. Korisnik može definisati ciljeve učenja, dok klasa prati status izvršenja (progres) i čuva planove u bazi, čime se postiže kontinuitet u radu i bolja organizacija vremena za studentske obaveze.

4. Navigacija i UI/UX dizajn
MainMenu
Glavni navigacioni panel koji služi kao interfejs između korisnika i svih pod sistema. Implementira Event Listener logiku za pokretanje specifičnih formi, istovremeno kontrolišući životni ciklus prozora (otvaranje, zatvaranje i oslobađanje resursa).

Dizajn interfejsa (IntelliJ GUI Designer)
Sve forme su dizajnirane korišćenjem .form XML deskriptora, što omogućava jasnu separaciju dizajna od poslovne logike koda. Ovakav pristup osigurava responzivnost komponenti i olakšava kasnije nadogradnje interfejsa bez rizika od kvarenja backend koda.
