 


# TODO:
- [ ] Criar novo "registro", representando-o através de seu respectivo objeto e armazenando-o junto dos outros objetos previamente criados.
 Os campos obrigatórios para cada tipo de registro são
Acesso: placa, data de entrada e saida, hora de entrada e saida;
Estacionamento: capacidade vagas, valor fracao 15 minutos, valor hora cheia, valor diaria diurna, valor diaria noturna (em %)
Evento: nome do evento, data de inicio e fim, hora de inicio e fim.
Caso algum dos campos obrigatórios não tiver sido informado, uma exceção do tipo DescricaoEmBrancoException deverá ser lançada e o objeto não deverá ser incluído no cadastro.


- [ ] Pesquisar registro através de campos dos objetos a serem pesquisados. Cada uma das pesquisas (acesso, estacionamento e evento) tem o seu conjunto de campos a serem pesquisados e que estão listados a seguir:

Acesso: placa,
Estacionamento: nome do estacionamento
Evento: nome do evento
Caso um objeto não tenha sido encontrado dentre o conjunto de objetos, uma exceção do tipo ObjetoNãoEncontradoException deverá ser lançada e capturada pelo programa.

- [ ] Alterar os dados de um cadastro (acesso, estacionamento e evento)já existente. Nesse caso o objeto do cadastro deverá ser recuperado através da função de pesquisa do registro e os novos dados deverão ser atualizados no objeto recuperado pela busca.

- [ ] Excluir cadastro, através da busca do objeto através da função pesquisar.