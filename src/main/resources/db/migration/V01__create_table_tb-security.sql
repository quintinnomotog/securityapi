create table if not exists tb_usuario (
	code varchar(255) not null comment 'Representa o identificador único da tabela',
	nome varchar(255) not null comment 'Representa nome do usuário',
	identificador varchar(255) not null comment 'Representa identificador (pode ser e-mail ou telefone) do usuário',
	senha varchar(255) not null comment 'Representa o hash da senha do usuário',
	active boolean not null default true comment 'Representa a situação do cadastro que pode ser ativo ou inativo',
    created_at datetime not null default current_timestamp comment 'Representa a data de criação do registro',
    updated_at datetime not null default current_timestamp comment 'Representa a data de alteração do registro',
    deleted_at datetime null comment 'Representa a data de exclusão (não física) do registro',
    constraint pk_pessoa_code primary key (code)
) comment = 'Responsável por armazenar dados dos usuários do sistema';