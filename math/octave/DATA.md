Merjenja in komentarji
======================

Podatki o meritvah
------------------

Podatki o meritvah so zajeti v datoteki csv (coma separated values).
Prve tri vrednosti so odčitki accelerometra, zadnja pa je timestamp.
Vrednosti Accelerometa so umerjene v m/s^2, timestamp pa je v nanosekundah
in sicer od 1.1.1970 naprej.

Primer:

    0.313268, -0.081722, 9.997335, 28892178869000
    0.313268, -0.081722, 10.119919, 28892397161000
    0.340509, 0.000000, 10.079058, 28892616582000

Izračuni
--------

Podatke najprej časovno normaliziramo tako, da se začno ob času 0
in da je čas podan v sekundah. Temu signalu rečemo `acc`.

Glede na to, da sta v odčitku accelerometra združena dva signala,
gravitacijski pospešek in pospešek, ki povzroča premikanje telefona,
poskusimo najprej ločiti signal na gravitacijo (`gravitation`) 
in linearni pospešek (`linacc`).

    ALPHA := 0.8
    gravitation := lowpass(acc, ALPHA)
    linacc := acc - gravitation

Kako dobro smo uspeli ločiti signala lahko preverimo, če
izračunamo absolutne vrednosti gravitacijskega signal, ki mora
biti čim bližje 9.81 m/s^2.

Nizkopasovni (Low pass) filter
------------------------------

Low pass filter 1. reda izračunamo z:

    y[0] := x[0]
    y[i] := ALFA*x[i] + (1-ALFA)*y[i-1]

Manjši kot je ALFA, večja je inertnost filtr oz. v novi vrednosti
signala bolj prevladuje prejšnja vrednost filtra kot pa trenutni vhod.

Glej tudi: http://en.wikipedia.org/wiki/Low-pass_filter


Datoteke z vmesnimi rezlutati
-----------------------------

`start.txt`             vsebuje odčitke accelerometra in timestamp
`acc.txt`               pospešek z odstranjenim časom (za dt se vame povprečno razdaljo med vzorci)
`gravity.txt`           acc.txt --> lowpass_filter  (gravitacija)
`linacc.txt`            od acc odšteta gravitacija
`delta_velocity.txt`    trenutna hitrost linacc*dt
`velocity.txt`          akumulirana hitrost
`delta_distance.txt`    izračun premika v času dt (velocity*dt+linacc * 1/2*dt^2)
`distance.txt`          akumulirana razdalja (pozicija)
`distance_abc.txt`      absolutna dolžina vektroja pozicija

Vzorčna frekvneca je 312 Hz
ALFA low pass filtra 0.8

Meritve
-------
`/still/`        meritev telefona v mirovanju, na začetku drsne površine
`/line_115cm/    meritev približno enakomernega drsenja telefona po 115 m dolgi vodoravni površini

Težave
------

http://www.motusbioengineering.com/accelerometer-limitations.htm

  "The piezoelectric accelerometer is less expensive and can fit on one's hand but cannot measure either static accelerations or ones occurring at the low frequencies commonly found in human motion."

Iz tega izhaja, da je accelerometer izrazito neprimeren za merjenje pozicije telefona na podlagi premikov, ki jih povzroča človeška roka in imajo nizke frekvence, tako kot gravitacija.

Mogoče tale post prinaša malo upanja:

http://stackoverflow.com/questions/2674717/how-to-detect-iphone-movement-in-space-using-accelerometer

Sfiltrirano gravitacijo je treba namreč "normalizirati" glede na prvotni signal. Uporabil sem mediano vrednosti po vsaki osi in rezultati so bolj vzpodbudni.

Ključna novost je:

    gravity = [ cebisev(a, b, acc(:, 1)),  cebisev(a, b, acc(:, 2)), cebisev(a, b, acc(:, 3))];
    gravity = [gravity(:,1) * median(acc(:,1))/median(gravity(:,1)), \ 
         gravity(:,2) * median(acc(:,2))/median(gravity(:,2)),   \
         gravity(:,3) * median(acc(:,3))/median(gravity(:,3))];

