CREATE TABLE public.users_properties
(
    id uuid NOT NULL,
    company_id uuid NOT NULL,
    domain character varying(255) NOT NULL,
    key character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    PRIMARY KEY (id),
    FOREIGN KEY (company_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
        NOT VALID
);

ALTER TABLE IF EXISTS public.users_properties
    OWNER to postgres;