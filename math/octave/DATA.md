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


