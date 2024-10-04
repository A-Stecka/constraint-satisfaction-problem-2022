# Rozwiązywanie łamigłówek binary oraz Futoshiki rozumianych jako problem CSP (Constraint Satisfaction Problem)
Rozwiązywanie łamigłówek binary i Futoshiki wykonane w ramach przedmiotu Sztuczna Inteligencja i Inżynieria Wiedzy
-

Constraint Satisfaction Problem – problem spełniania ograniczeń. Rozwiązaniem problemu typu CSP jest takie przyporządkowanie wartości do zmiennych, dla którego wszystkie ograniczenia są spełnione. Ograniczenia są zdefiniowane dla konkretnego problemu.

Definicja CSP dla łamigłówki binary:
- dziedzina problemu: plansza n x n,
- zmienne: n^2 pól planszy,
- dziedziny zmiennych: {0, 1},
- ograniczenia:
  - w wierszu i kolumnie musi wystąpić tyle samo wartości 0 co 1 – tzn. w wierszu i kolumnie nie może wystąpić więcej zer lub jedynek niż wynosi połowa 𝑛,
  - wiersze muszą być unikatowe między sobą,
  - kolumny muszą być unikatowe między sobą,
  - ani w poziomie, ani w pionie nie mogą wystąpić trzy sąsiednie wartości 0 ani 1.

Definicja CSP dla łamigłówki Futoshiki:
- dziedzina problemu: plansza n x n,
- zmienne: n^2 pól planszy,
- dziedziny zmiennych: {0, 1},
- ograniczenia:
  - wartość nie może powtórzyć się w wierszu,
  - wartość nie może powtórzyć się w kolumnie,
  - ograniczenia wynikające ze znaków >,< wpisanych między polami planszy.
