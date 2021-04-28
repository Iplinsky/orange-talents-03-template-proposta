INSERT INTO PROPOSTA(id, documento, email, nome,  endereco, salario, estado_da_proposta) 
VALUES (1, '70942469003', 'teste-um@gmail.com', 'Primeiro usuario', 'Rua teste do primeiro usuario, nr 1', 2500.0, 'CONCLUIDA');

INSERT INTO PROPOSTA(id, documento, email, nome,  endereco, salario, estado_da_proposta) 
VALUES (2, '31188304097', 'teste-dois@gmail.com', 'Segundo usuario', 'Rua teste do segundo usuario, nr 2', 1500.0, 'ELEGIVEL');

INSERT INTO CARTAO(id, emitido_em, limite, nr_cartao, status_bloqueio_cartao , titular, proposta_id)
VALUES (1,' 2021-04-25T22:24:10.08929', 5000, '6739-3318-6961-6970', 'DESBLOQUEADO', 'Primeiro usuario', '1');

INSERT INTO CARTAO(id, emitido_em, limite, nr_cartao, status_bloqueio_cartao , titular, proposta_id)
VALUES (2,' 2021-04-25T22:25:10.08929', 7000, '5209-1622-1164-1234', 'BLOQUEADO', 'Segundo usuario', '2');