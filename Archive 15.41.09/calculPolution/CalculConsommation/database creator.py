import pandas as pd
import sqlite3
exceldb = pd.read_excel('data/voitures-complet.xls', header = 0)

print(exceldb.columns)
db_conn = sqlite3.connect("data/voitures.db")
c = db_conn.cursor()
c.execute(
    """
CREATE TABLE voitures (
    "index" INTEGER PRIMARY KEY,
    lib_mrq_doss TEXT NOT NULL,
    lib_mod_doss TEXT NOT NULL,
    mrq_utac TEXT NOT NULL,
    mod_utac TEXT NOT NULL,
    dscom TEXT NOT NULL,
    cnit TEXT,
    tvv TEXT,
    energ TEXT,
    hybride TEXT,
    puiss_admin REAL,
    puiss_max REAL,
    puiss_heure REAL,
    typ_boite_nb_rapp TEXT,
    conso_urb_93 REAL,
    conso_exurb REAL,
    conso_mixte REAL,
    co2_mixte REAL,
    co_typ_1 REAL,
    hc REAL,
    nox REAL,
    hcnox REAL,
    ptcl REAL,
    masse_ordma_min REAL,
    masse_ordma_max REAL,
    champ_v9 TEXT,
    date_maj TEXT
    );
"""
)

exceldb.to_sql('voitures', db_conn, if_exists='append', index=True)