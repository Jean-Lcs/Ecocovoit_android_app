import sqlite3

db_conn = sqlite3.connect("data/voitures.db")
c = db_conn.cursor()

c.execute( """ SELECT "index", lib_mrq_doss, lib_mod_doss, energ FROM voitures where lib_mrq_doss = "TESLA " """ )
# Exemple de requettes :
# "SELECT lib_mrq_doss, lib_mod_doss, puiss_max FROM voitures ORDER BY puiss_max"
# "SELECT lib_mrq_doss, lib_mod_doss FROM voitures WHERE lib_mrq = 'RENAULT'"
# "SELECT lib_mrq_doss, lib_mod_doss, conso_mixte FROM voitures ORDER BY conso_mixte"
# "SELECT lib_mrq_doss, lib_mod_doss FROM voitures WHERE hybride = 'oui'"
# "SELECT lib_mrq_doss, lib_mod_doss FROM voitures WHERE lib_mrq = 'CITROEN'"
for row in c:
    print(row)