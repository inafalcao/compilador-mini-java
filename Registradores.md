# Alocação de Registradores #

Foi construído o grafo de interferência onde os nós são os temporários utilizados nas instruções e os registradores. Se existe aresta entre dois nós, representa que os nós estão vivos ao mesmo tempo na execução do programa e eles não podem ser alocados no mesmo registrador. Obtemos essa informação devido a computação de longevidade executada na etapa anterior. Essa fase tenta colorir o grafo com k cores, que é o número de registradores da máquina, de tal forma que os nós que interferem não possuam a mesma cor. Os nós que não poderem ser coloridos, serão colocados em memória. Outra função dessa fase é eliminar instruções MOVE quando possível, colocando a origem e o destino dessa instrução no mesmo registrador.

Os algoritmos de construção do grafo de interferência, simplify, coalesce, freeze, potencial spill, select, actual spill e start over foram implementados.

Esta fase não foi realizada, visto que os membros da equipe não tinham mais tempo para a finalização do compilador.

<b>Implementação:</b> <font color='#1E90FF'> não implementada </font>

<b>Funcionalidade Implementadas:</b> <font color='#1E90FF'> nenhuma</font>

<b>Funcionalidades Remanescentes:</b> <font color='#1E90FF'> registro de alocação </font>

<b>Testes:</b> <font color='#1E90FF'>nenhum.</font>

<b>Aval Final:</b> <font color='#1E90FF'> não foi possível implementarmos esse módulo, visto que ficamos aperriados com o final do semestre e todos da equipe tinha muitos trabalho relacionados com as outras disciplinas e muitas provas</font>

<div><a href='http://code.google.com/p/compilador-mini-java/wiki/Longevidade'>preview</a> | <a href='http://code.google.com/p/compilador-mini-java/wiki/Testes'>next</a>
</div>