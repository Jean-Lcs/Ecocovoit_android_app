import numpy as np
import matplotlib.pyplot as plt

## Variables

## Toyota Yaris Verso Linea Sol 1.5 (moteur 1NZ-FE)
# masse de la voiture plus passagers (kg)
m=1090+80
# maître couple (surface frontale) (m^2)
S=2.40
# coefficient de traînée
Cx=0.32
# coefficient de résistance au roulement (pneu/route)
k=0.015
# diamètre de la roue (m)
d=0.58
# rendement de la transmission (77% roue/sol, boite de vitesse (.95^2),
# différentiel (.95)
rt=.66
#cylindree en L
cyl = 1.2


## Constantes
# accélération de la pesanteur (m/s^2)
g=9.81
# masse volumique de l'air (kg/m^3)
rho=1.20
# masse volumique de l'essence (g/L)
masse_vol_ess=734

# Force de résistance de l'air (N), v en km/h
def Fx(v):
    return 0.5*rho*S*Cx*(v/3.6)**2
# Force de résistance du roulement (N)
def Fr(penteEnPourcent):
    return m*g*k*np.cos(np.arctan(penteEnPourcent/100.0))
# Force du poids à cause de la pente
def Fp(penteEnPourcent):
    return m*g*np.sin(np.arctan(penteEnPourcent/100.0))

# Couple nécessaire pour vaincre le frottement (N.m), v en km/h
def C(v, penteEnPourcent):
    return (Fx(v)+Fr(penteEnPourcent)+Fp(penteEnPourcent))*d/2

# Pression moyenne effective (bar), v en km/h, rpm en tr/min
def pme(v, rpm, penteEnPourcent):
    return (Fx(v)+Fr(penteEnPourcent)+Fp(penteEnPourcent))*v/(3*rt*cyl*rpm)

# Pour une vitesse donnee v, calcul du rgime moteur et du rapport

def rpmVitesseDonnee(v):
    Rm5 = (v*1000*4.058*0.864)/(60*d*3.14)
    Rm4 = (v*1000*4.058*1.031)/(60*d*3.14)
    Rm3 = (v*1000*4.058*1.310)/(60*d*3.14)
    Rm2 = (v*1000*4.058*1.904)/(60*d*3.14)
    Rm1 = (v*1000*4.058*3.545)/(60*d*3.14)
    
    if v <= 15: return Rm1
    if v <= 30: return Rm2
    if v <= 46 : return Rm3
    if v <= 60 : return Rm4
    return Rm5
    

# Consommation spécifique du moteur (g/kWh), v en km/h, rpm en tr/min
def csp(v, rpm, penteEnPourcent):
    return  (((rpm-2500)*.0025)**2+((pme(v,rpm, penteEnPourcent)-9)*.5)**4)+248.5

def consoLau100(v, rpm,penteEnPourcent):
    return (Fx(v)+Fr(penteEnPourcent)+Fp(penteEnPourcent))*csp(v,rpm, penteEnPourcent)/(36*masse_vol_ess*rt)

X = [v for v in range(0,135)] # vitesses a tester
Y = [consoLau100(v,rpmVitesseDonnee(v),0) for v in X]
plt.plot(X,Y)
plt.show()



# Convenir d'une date
# Explcation de ce qu'on a fait


# Pour nous : 
# Utiliser ce script en changer la cylindre : une grosse voiture a une plus grosse cylindree
# donc elle devrait consommer plus ? 
# Fonction qui calcul la consommation totale d'un trajet
# Schema d'architecture  : entrees / sorties
# Attribuer un score au voiture en fonction de l'energie / consommation / hybride ou non / ... 