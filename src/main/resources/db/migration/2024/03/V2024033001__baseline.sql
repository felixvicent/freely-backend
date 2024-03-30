--
--PostgreSQL database dump
--

-- Dumped from database version 16.1 (Debian 16.1-1.pgdg120+1)
-- Dumped by pg_dump version 16.1 (Debian 16.1-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: activities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.activities (
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL,
    status character varying(255) NOT NULL,
    title character varying(255) NOT NULL,
    updated_at timestamp without time zone,
    project_id uuid NOT NULL,
    estimated_date timestamp without time zone NOT NULL,
    finished_at timestamp without time zone,
    company_id uuid NOT NULL,
    responsible_id uuid,
    description text
);


--
-- Name: addresses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.addresses (
    id uuid NOT NULL,
    city character varying(255) NOT NULL,
    complement character varying(255),
    number character varying(255) NOT NULL,
    reference character varying(255),
    state character varying(255) NOT NULL,
    street character varying(255) NOT NULL,
    zip_code character varying(255) NOT NULL
);


--
-- Name: clients; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.clients (
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL,
    document character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    telephone character varying(255) NOT NULL,
    updated_at timestamp without time zone,
    address_id uuid,
    company_id uuid NOT NULL,
    name character varying(255) NOT NULL
);

--
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comments (
    id uuid NOT NULL,
    comment text,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone,
    activity_id uuid NOT NULL,
    user_id uuid NOT NULL
);


--
-- Name: projects; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.projects (
    id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL,
    estimated_date timestamp without time zone NOT NULL,
    title character varying(255) NOT NULL,
    updated_at timestamp without time zone,
    value float8 NOT NULL,
    client_id uuid NOT NULL,
    company_id uuid NOT NULL,
    status character varying(255) NOT NULL
);


--
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    id uuid NOT NULL,
    name character varying(255)
);


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    active boolean NOT NULL,
    avatar character varying(255),
    created_at timestamp without time zone NOT NULL,
    document character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    telephone character varying(255),
    updated_at timestamp without time zone,
    office character varying(255),
    company_id uuid
);


--
-- Name: users_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_roles (
    user_account_id uuid NOT NULL,
    roles_id uuid NOT NULL
);


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (id, name) FROM stdin;
04537612-70aa-4439-b679-1b836ef8869d	ADMIN
7e5600f1-62c1-4694-8441-e22e50692f43	USER
2ea5261d-fc99-4146-ac17-3c24a5ed93b9	COMPANY
\.


--
-- Name: activities activities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.activities
    ADD CONSTRAINT activities_pkey PRIMARY KEY (id);


--
-- Name: addresses addresses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.addresses
    ADD CONSTRAINT addresses_pkey PRIMARY KEY (id);


--
-- Name: clients clients_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT clients_pkey PRIMARY KEY (id);


--
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- Name: projects projects_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.projects
    ADD CONSTRAINT projects_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: users uk_6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: users uk_kgnb8tmua5cirywqjj4m3id0l; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_kgnb8tmua5cirywqjj4m3id0l UNIQUE (document);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users_roles users_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT users_roles_pkey PRIMARY KEY (user_account_id, roles_id);


--
-- Name: clients fk21gyuophuha3vq8t1os4x2jtl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT fk21gyuophuha3vq8t1os4x2jtl FOREIGN KEY (address_id) REFERENCES public.addresses(id);


--
-- Name: projects fk4dywpo7n4v54vlg61amqtdnpa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.projects
    ADD CONSTRAINT fk4dywpo7n4v54vlg61amqtdnpa FOREIGN KEY (company_id) REFERENCES public.users(id);


--
-- Name: users_roles fk5vql0x05rjd3x7mgbxpow0mm5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fk5vql0x05rjd3x7mgbxpow0mm5 FOREIGN KEY (user_account_id) REFERENCES public.users(id);


--
-- Name: users fk5x3oxgbg4lcyk324731xb5xgx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fk5x3oxgbg4lcyk324731xb5xgx FOREIGN KEY (company_id) REFERENCES public.users(id);


--
-- Name: comments fk82931cwhgrnep4qa3e87ey2lr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fk82931cwhgrnep4qa3e87ey2lr FOREIGN KEY (activity_id) REFERENCES public.activities(id);


--
-- Name: comments fk8omq0tc18jd43bu5tjh6jvraq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fk8omq0tc18jd43bu5tjh6jvraq FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: activities fk92qddhofup60p5g7y21fh8drq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.activities
    ADD CONSTRAINT fk92qddhofup60p5g7y21fh8drq FOREIGN KEY (company_id) REFERENCES public.users(id);


--
-- Name: users_roles fka62j07k5mhgifpp955h37ponj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fka62j07k5mhgifpp955h37ponj FOREIGN KEY (roles_id) REFERENCES public.roles(id);


--
-- Name: clients fkb1opsa9i448d5al479607tieu; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clients
    ADD CONSTRAINT fkb1opsa9i448d5al479607tieu FOREIGN KEY (company_id) REFERENCES public.users(id);


--
-- Name: activities fkby2clskbqbtxyqqs6s0orkopf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.activities
    ADD CONSTRAINT fkby2clskbqbtxyqqs6s0orkopf FOREIGN KEY (responsible_id) REFERENCES public.users(id);


--
-- Name: projects fkksdiyuily2f4ca2y53k07pmq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.projects
    ADD CONSTRAINT fkksdiyuily2f4ca2y53k07pmq FOREIGN KEY (client_id) REFERENCES public.clients(id);


--
-- Name: activities fksp1gle1x16hi1viq0vjx26hmf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.activities
    ADD CONSTRAINT fksp1gle1x16hi1viq0vjx26hmf FOREIGN KEY (project_id) REFERENCES public.projects(id);


--
-- PostgreSQL database dump complete
--
