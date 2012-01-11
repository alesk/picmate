% plots 2 3d signals side by si
source ("utils.m");

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% load data from csv file
%start = load("-ascii", "measurements/still/start.txt");
start = load("-ascii", "measurements/line_115cm/start.txt");
%start = load("-ascii", "measurements/line_115cm/start_cut_20.txt");


% loči čas in koordinate
acc = start(:, [1,2,3]);
time_raw = start(:, 4);

% normalizira čas
time = (time_raw .- time_raw(1)) ./ 1E9;

% number of samples
N = length(time);

dt = time(N)/N;
sfreq = 1/dt;

% izriše pospeševanje v času
% zaporedje barv (modra, zelena, rdeča)
% plot(time, acc(:,1), time, acc(:,2), time, acc(:,3))

% loči signal na gravitacijo (gravity) in lienarni pospešek (linacc)
% Fnyq (nyquist frequency is half the sample frequency
% Fc .. cut-off frequency 0 < Fc < Fnyq
Fnyq = sfreq/2;
Fc =0.8;
Fc/Fnyq
[b, a] = butter(2,Fc/Fnyq);

% separate signal to gravity and lin acc
gravity = filter(b, a, acc);
linacc_raw = acc - gravity;

% compute velocity
velocity = cumsum(linacc .* dt);
distance = cumsum((velocity .* dt)  + (linacc .* (dt*dt/2)));


plotme(time, acc, gravity, "pospeskomer", "gravitcija");
plotme(time, gravity, linacc, "gravitcija", "linpospesek");
plotme(time, acc, linacc, "pospeskomer", "linpospesek");
plotme(time, linacc, velocity, "linpospesek", "hitrost");
plotme(time, velocity, distance, "hitrost", "razdalja");
