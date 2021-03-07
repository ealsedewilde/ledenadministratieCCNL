create sequence hibernate_sequence start with 1 increment by 1

    create table archived_member (
       archive_year integer not null,
        member_number integer not null,
        address varchar(255) not null,
        address_invalid boolean not null,
        address_number varchar(255) not null,
        address_number_appendix varchar(255),
        city varchar(255) not null,
        country varchar(255),
        postal_code varchar(255),
        current_year_paid boolean not null,
        email varchar(255),
        iban_number varchar(255),
        initials varchar(255) not null,
        last_name varchar(255) not null,
        last_name_prefix varchar(255),
        member_info clob,
        member_since date,
        member_status varchar(255) not null,
        membercard_issued boolean not null,
        modification_date date,
        no_magazine boolean not null,
        payment_date date,
        payment_info clob,
        payment_method varchar(255) not null,
        telephone_number varchar(255),
        primary key (archive_year, member_number)
    )

    create table direct_debit_config (
       id integer not null,
        auth_description varchar(255),
        auth_value varchar(255),
        auth_type_description varchar(255),
        auth_type_value varchar(255),
        iban_name_description varchar(255),
        iban_name_value varchar(255),
        dd_amount_description varchar(255),
        dd_amount_value decimal(19,2),
        dd_date_description varchar(255),
        dd_date_value date,
        dd_reason_description varchar(255),
        dd_reason_value varchar(255),
        dd_dir_description varchar(255),
        dd_dir_value varchar(255),
        dd_id_description varchar(255),
        dd_id_value varchar(255),
        iban_number_description varchar(255),
        iban_number_value varchar(255),
        msg_id_description varchar(255),
        mssg_id_value varchar(255),
        testrun_description varchar(255),
        testrun_value boolean,
        primary key (id)
    )

    create table document (
       id integer not null,
        base64content clob not null,
        creation_date date not null,
        description varchar(255),
        document_name varchar(255) not null,
        document_type varchar(255) not null,
        owner_id integer,
        primary key (id)
    )

    create table document_template (
       document_template_type varchar(255) not null,
        name varchar(255) not null,
        include_sepa_form boolean not null,
        modification_date date not null,
        template clob not null,
        primary key (document_template_type, name)
    )

    create table external_relation (
       relation_type varchar(31) not null,
        relation_number integer not null,
        address varchar(255) not null,
        address_invalid boolean not null,
        address_number varchar(255) not null,
        address_number_appendix varchar(255),
        city varchar(255) not null,
        country varchar(255),
        postal_code varchar(255),
        contact_name varchar(255) not null,
        contact_name_prefix varchar(255),
        email varchar(255),
        modification_date date,
        relation_info clob,
        relation_name varchar(255),
        relation_since date,
        telephone_number varchar(255),
        primary key (relation_number)
    )

    create table internal_relation (
       relation_number integer not null,
        address varchar(255) not null,
        address_invalid boolean not null,
        address_number varchar(255) not null,
        address_number_appendix varchar(255),
        city varchar(255) not null,
        country varchar(255),
        postal_code varchar(255),
        contact_name varchar(255) not null,
        modification_date date,
        telephone_number varchar(255),
        title varchar(255) not null,
        primary key (relation_number)
    )

    create table member (
       member_number integer not null,
        address varchar(255) not null,
        address_invalid boolean not null,
        address_number varchar(255) not null,
        address_number_appendix varchar(255),
        city varchar(255) not null,
        country varchar(255),
        postal_code varchar(255),
        current_year_paid boolean not null,
        email varchar(255),
        iban_number varchar(255),
        initials varchar(255) not null,
        last_name varchar(255) not null,
        last_name_prefix varchar(255),
        member_info clob,
        member_since date,
        member_status varchar(255) not null,
        membercard_issued boolean not null,
        modification_date date,
        no_magazine boolean not null,
        payment_date date,
        payment_info clob,
        payment_method varchar(255) not null,
        telephone_number varchar(255),
        primary key (member_number)
    )

    create table payment_file (
       file_name varchar(255) not null,
        xml clob,
        primary key (file_name)
    )

    create table setting (
       id varchar(255) not null,
        description varchar(255),
        settings_key varchar(255) not null,
        settings_group varchar(255),
        settings_value varchar(255) not null,
        primary key (id)
    )

    alter table internal_relation 
       add constraint UK_oaq592pqxq05roqhes5tu5ynd unique (title)

    alter table document 
       add constraint FK7uh6e2lxbp5r6ech54je7ixl1 
       foreign key (owner_id) 
       references member
