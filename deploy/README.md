# Wdrożenie warsztatu w chmurze (DigitalOcean / dowolny VPS)

Maszyna jest **efemeryczna** — nic wrażliwego, kasujesz po warsztacie. Otwarty JDWP =
zdalne wykonanie kodu, więc domyślnie **nie wystawiamy portów debuggera na świat**:
bindujemy je na loopback, a uczestnicy dochodzą do nich tunelem SSH.

## Wariant A — ręcznie (polecany za pierwszym razem)
Utwórz Droplet: Ubuntu 24.04, 8 GB/4 vCPU, klucz SSH. Potem:
```bash
ssh root@IP_DROPLETA

# zależności
apt-get update && apt-get install -y git docker.io docker-compose-v2 openjdk-21-jdk
systemctl enable --now docker

# repo + build + 30 instancji (JDWP tylko na loopbacku)
git clone REPO_URL /opt/workshop
cd /opt/workshop
./mvnw -q -pl 04-jdwp-ctf -am package
cd 04-jdwp-ctf
./scale.sh 30 127.0.0.1
docker compose -f compose.scale.yaml up --build -d

# firewall: tylko SSH + HTTP do /verify
ufw allow 22/tcp
ufw allow 8081:8110/tcp
ufw --force enable
```
Sprawdź przydział flag: `docker compose -f compose.scale.yaml exec ctf-07 printenv WORKSHOP_FLAG`.

## Wariant B — automat (cloud-init)
Wklej `cloud-init.yaml` (z podmienionym `REPO_URL`) w polu **user data** przy tworzeniu
Droplet-a. Stawia to samo co wariant A.

## Dostęp uczestnika (przez tunel SSH)
Uczestnik przekierowuje swój port do loopbacku serwera i wpina debugger na localhost:
```bash
ssh -N -L 5006:localhost:5006 user@IP_DROPLETA     # dla instancji ctf-01
```
Potem: *Remote JVM Debug* → host `localhost`, port `5006`. Flaga do sprawdzenia:
`http://IP_DROPLETA:8081/verify?flag=...` (HTTP jest otwarte wprost).

| Osoba | HTTP (wprost) | JDWP (przez tunel do loopbacku) |
|-------|---------------|----------------------------------|
| 1 | IP:8081 | localhost:5006 |
| 2 | IP:8082 | localhost:5007 |
| … | … | … |
| 30 | IP:8110 | localhost:5035 |

> Uwaga o CTF: uczestnik z powłoką SSH na serwerze może odczytać flagi `printenv`-em.
> Jeśli robisz to jako uczciwy CTF, dawaj dostęp tunelujący bez powłoki (SSH
> `ForceCommand`/`no-pty`, wyłączone forwardowanie poza potrzebnym portem), albo hostuj
> u siebie i udostępniaj wyłącznie porty, nie konta.

## Wystawienie JDWP wprost (świadoma decyzja prowadzącego)
Jeśli świadomie chcesz, by uczestnicy wpinali debugger bez tunelu (np. zaufany LAN na
sali), przegeneruj z bindem `0.0.0.0` (`./scale.sh 30 0.0.0.0`) i sam dodaj regułę
firewalla na porty debuggera. Pamiętaj: **to udostępnia zdalne wykonanie kodu na tej
maszynie** — rób to tylko na jednorazowym hoście bez niczego wrażliwego i skasuj go po
warsztacie. Ten zabieg celowo nie jest zaszyty w skryptach.

## PO WARSZTACIE — WAŻNE
DigitalOcean nalicza za Droplet nawet wyłączony. Zatrzymanie kosztów = **Destroy**:
```bash
docker compose -f compose.scale.yaml down    # opcjonalnie
```
a potem w panelu DO: **Destroy Droplet** (nie samo "Power off").
