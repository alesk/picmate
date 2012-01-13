% plots 2 3d signals side by si
source ("utils.m");

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% load data from csv file
%start = load("-ascii", "measurements/still/start.txt");
%start = load("-ascii", "measurements/line_115cm/start_cut_20.txt");

%start_raw = load("-ascii", "../measurements/line_115cm/start.txt");
%start = start_raw((400:length(start)),:)

start = load("-ascii", "../measurements/line_115cm/start.txt");


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

% separate signal to gravity and lin acc

% gravity butterworth
% loči signal na gravitacijo (gravity) in lienarni pospešek (linacc)
% Fnyq (nyquist frequency is half the sample frequency
% Fc .. cut-off frequency 0 < Fc < Fnyq
% Fnyq = sfreq/2;
% Fc =0.8;
% Fc/Fnyq
% [b, a] = butter(2,Fc/Fnyq);
% 
% gravity = filter(b, a, acc);


% gravitiy cebisev

a = [1.0000, -1.9536, 0.9546];
b = [0.2634, 0.5268, 0.2634] *0.001;
gravity = [ cebisev(a, b, acc(:, 1)),  cebisev(a, b, acc(:, 2)), cebisev(a, b, acc(:, 3))];
gravity = [gravity(:,1) * median(acc(:,1))/median(gravity(:,1)), \ 
         gravity(:,2) * median(acc(:,2))/median(gravity(:,2)),   \
         gravity(:,3) * median(acc(:,3))/median(gravity(:,3))];

linacc = acc - gravity;

% compute velocity
velocity = cumsum(linacc .* dt);
distance = cumsum((velocity .* dt)  + (linacc .* (dt*dt/2)));

figure(1)
plotme(time, acc, gravity, "pospeskomer", "gravitcija");
figure(2)
plotme(time, gravity, linacc, "gravitcija", "linpospesek");
plotme(time, acc, linacc, "pospeskomer", "linpospesek");
plotme(time, linacc, velocity, "linpospesek", "hitrost");
%plotme(time(400:N), velocity(400:N,:), distance(400:N,:), "hitrost", "razdalja");
plotme(time, velocity, distance, "hitrost", "razdalja");

% spekter distance
% http://www.mathworks.com/support/tech-notes/1700/1702.html

nfft = 2^(nextpow2(N));
linacc_fft = abs(fft(linacc, nfft));
velocity_fft = abs(fft(velocity, nfft));
distance_fft = abs(fft(distance, nfft));

figure(3)
subplot(3,1,1)
semilogy(linacc_fft)
title("Pospesek")
subplot(3,1,2)
semilogy(velocity_fft)
title("Hitrost")
subplot(3,1,3)
semilogy(distance_fft)
title("Razdalja")
print("spectral_components.png")

figure(4)
plot(time, [acc(:,3), gravity(:,3), linacc(:,3) ])



