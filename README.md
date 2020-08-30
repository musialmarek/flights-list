#Zadanie 1 
1. Stwórz projekt Spring-Boot
2. Stwórz repozytorium na GitHub
3. Uzupełnij pliki .gitignore pom.xml i application.properties 
4. Stwórz plik README.md
5. Stwórz bazę danych 
#Zadanie 2
1. Stwórz Encję Pilot 
posiadającą właściwości :
Pilot
	- id
	- nazwa (String)
	- aeroklub (String)
	- nrLicencji (String)
	- aktywny (boolean)
	- szybowcowy (boolean)
	- instrSzybowcowy (boolean)
	- samolotowy (boolean)
	- instrSamolotowy (boolean)
	- holownik (boolen)
	- nalot? (Long)
	- badania (LocalDate)
	- saldo? (BigDecimal)
	- KWT (LocalDate)
	- KTP_szyb (LocalDate)
	- KTP_SAM (LocalDate)
	- SEPL (LocalDate)
	- FI(S) (LocalDate)
	- FI(A) (LocalDate)
2. Stwórz PilotRepository do połączenia z bazą danych
3. Stwórz PilotService zawierający metody dodawania edytowania i dezaktywowania Pilota 
4. Stwórz PilotController z akcjami do wyświetlania widoków listy pilotów, dodawania/edycji pilota, aktywacji/dezaktywacji pilota 
5. Stwórz widoki wyświetlające wszystkich pilotów, dodawania pilota, edycji pilota, na liście pilotów dodaj przycisk aktywujący/dezaktywujący pilota
