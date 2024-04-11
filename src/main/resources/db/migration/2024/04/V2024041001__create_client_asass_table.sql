CREATE TABLE public.clients_asaas
(
    id uuid,
    client_id uuid NOT NULL,
    asaas_customer_id character varying(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id)
        REFERENCES public.clients (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
        NOT VALID
);