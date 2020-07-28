--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.12
-- Dumped by pg_dump version 9.5.12

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: tri_insert_averia_abierta(); Type: FUNCTION; Schema: public; Owner: eduardo
--

CREATE FUNCTION public.tri_insert_averia_abierta() RETURNS trigger
    LANGUAGE plpgsql
    AS $$begin 
update averias_abiertas set ts_inicio=now() where id_averia_abierta=new.id_averia_abierta;

return null;
end;$$;


ALTER FUNCTION public.tri_insert_averia_abierta() OWNER TO eduardo;

--
-- Name: tri_insert_averia_cerrada(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.tri_insert_averia_cerrada() RETURNS trigger
    LANGUAGE plpgsql
    AS $$begin 
	update averias_cerradas set ts_fin=current_timestamp where id_averia_cerrada=new.id_averia_cerrada;

	return null;
end;$$;


ALTER FUNCTION public.tri_insert_averia_cerrada() OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: averias_abiertas; Type: TABLE; Schema: public; Owner: eduardo
--

CREATE TABLE public.averias_abiertas (
    id_averia_abierta bigint NOT NULL,
    id_averia bigint NOT NULL,
    ts_inicio timestamp without time zone,
    ts_acuse timestamp without time zone,
    id_tecnico_acusa bigint
);


ALTER TABLE public.averias_abiertas OWNER TO eduardo;

--
-- Name: averias_abiertas_id_averia_abierta_seq; Type: SEQUENCE; Schema: public; Owner: eduardo
--

CREATE SEQUENCE public.averias_abiertas_id_averia_abierta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.averias_abiertas_id_averia_abierta_seq OWNER TO eduardo;

--
-- Name: averias_abiertas_id_averia_abierta_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eduardo
--

ALTER SEQUENCE public.averias_abiertas_id_averia_abierta_seq OWNED BY public.averias_abiertas.id_averia_abierta;


--
-- Name: averias_cerradas; Type: TABLE; Schema: public; Owner: eduardo
--

CREATE TABLE public.averias_cerradas (
    id_averia_cerrada bigint NOT NULL,
    id_averia bigint NOT NULL,
    ts_inicio timestamp without time zone NOT NULL,
    ts_acuse timestamp without time zone,
    id_tecnico_acusa bigint,
    ts_fin timestamp without time zone
);


ALTER TABLE public.averias_cerradas OWNER TO eduardo;

--
-- Name: averias_cerradas_id_averia_cerrada_seq; Type: SEQUENCE; Schema: public; Owner: eduardo
--

CREATE SEQUENCE public.averias_cerradas_id_averia_cerrada_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.averias_cerradas_id_averia_cerrada_seq OWNER TO eduardo;

--
-- Name: averias_cerradas_id_averia_cerrada_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eduardo
--

ALTER SEQUENCE public.averias_cerradas_id_averia_cerrada_seq OWNED BY public.averias_cerradas.id_averia_cerrada;


--
-- Name: m_averias; Type: TABLE; Schema: public; Owner: eduardo
--

CREATE TABLE public.m_averias (
    id_averia bigint NOT NULL,
    area character varying(32) NOT NULL,
    subarea character varying(32) NOT NULL,
    sistema character varying(32) NOT NULL,
    elemento character varying(32) NOT NULL,
    prioridad integer NOT NULL,
    nombre character varying(32) NOT NULL,
    mensaje character varying(255) NOT NULL,
    descripcion character varying(1024) NOT NULL,
    actuacion character varying(1024) NOT NULL,
    id_tipo_averia bigint NOT NULL,
    zona character varying(32) NOT NULL
);


ALTER TABLE public.m_averias OWNER TO eduardo;

--
-- Name: averias_id_averia_seq; Type: SEQUENCE; Schema: public; Owner: eduardo
--

CREATE SEQUENCE public.averias_id_averia_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.averias_id_averia_seq OWNER TO eduardo;

--
-- Name: averias_id_averia_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eduardo
--

ALTER SEQUENCE public.averias_id_averia_seq OWNED BY public.m_averias.id_averia;


--
-- Name: m_receptores_averias; Type: TABLE; Schema: public; Owner: eduardo
--

CREATE TABLE public.m_receptores_averias (
    id_receptor_averias bigint NOT NULL,
    id_tecnico bigint NOT NULL,
    topic_receptor character varying(32) NOT NULL
);


ALTER TABLE public.m_receptores_averias OWNER TO eduardo;

--
-- Name: m_tecnicos; Type: TABLE; Schema: public; Owner: eduardo
--

CREATE TABLE public.m_tecnicos (
    id_tecnico bigint NOT NULL,
    nombre character varying(255) NOT NULL,
    apellido1 character varying(255) NOT NULL,
    apellido2 character varying(255),
    id_number character varying(9) NOT NULL
);


ALTER TABLE public.m_tecnicos OWNER TO eduardo;

--
-- Name: m_tipos_averia; Type: TABLE; Schema: public; Owner: eduardo
--

CREATE TABLE public.m_tipos_averia (
    id_tipo_averia bigint NOT NULL,
    nombre character varying(32) NOT NULL,
    descripcion character varying(255)
);


ALTER TABLE public.m_tipos_averia OWNER TO eduardo;

--
-- Name: receptores_averias_id_receptor_averias_seq; Type: SEQUENCE; Schema: public; Owner: eduardo
--

CREATE SEQUENCE public.receptores_averias_id_receptor_averias_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.receptores_averias_id_receptor_averias_seq OWNER TO eduardo;

--
-- Name: receptores_averias_id_receptor_averias_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eduardo
--

ALTER SEQUENCE public.receptores_averias_id_receptor_averias_seq OWNED BY public.m_receptores_averias.id_receptor_averias;


--
-- Name: receptores_averias_id_tecnico_seq; Type: SEQUENCE; Schema: public; Owner: eduardo
--

CREATE SEQUENCE public.receptores_averias_id_tecnico_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.receptores_averias_id_tecnico_seq OWNER TO eduardo;

--
-- Name: receptores_averias_id_tecnico_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eduardo
--

ALTER SEQUENCE public.receptores_averias_id_tecnico_seq OWNED BY public.m_receptores_averias.id_tecnico;


--
-- Name: tecnicos_id_tecnico_seq; Type: SEQUENCE; Schema: public; Owner: eduardo
--

CREATE SEQUENCE public.tecnicos_id_tecnico_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tecnicos_id_tecnico_seq OWNER TO eduardo;

--
-- Name: tecnicos_id_tecnico_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eduardo
--

ALTER SEQUENCE public.tecnicos_id_tecnico_seq OWNED BY public.m_tecnicos.id_tecnico;


--
-- Name: tipos_averia_id_tipo_averia_seq; Type: SEQUENCE; Schema: public; Owner: eduardo
--

CREATE SEQUENCE public.tipos_averia_id_tipo_averia_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tipos_averia_id_tipo_averia_seq OWNER TO eduardo;

--
-- Name: tipos_averia_id_tipo_averia_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: eduardo
--

ALTER SEQUENCE public.tipos_averia_id_tipo_averia_seq OWNED BY public.m_tipos_averia.id_tipo_averia;


--
-- Name: id_averia_abierta; Type: DEFAULT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.averias_abiertas ALTER COLUMN id_averia_abierta SET DEFAULT nextval('public.averias_abiertas_id_averia_abierta_seq'::regclass);


--
-- Name: id_averia_cerrada; Type: DEFAULT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.averias_cerradas ALTER COLUMN id_averia_cerrada SET DEFAULT nextval('public.averias_cerradas_id_averia_cerrada_seq'::regclass);


--
-- Name: id_averia; Type: DEFAULT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.m_averias ALTER COLUMN id_averia SET DEFAULT nextval('public.averias_id_averia_seq'::regclass);


--
-- Name: id_receptor_averias; Type: DEFAULT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.m_receptores_averias ALTER COLUMN id_receptor_averias SET DEFAULT nextval('public.receptores_averias_id_receptor_averias_seq'::regclass);


--
-- Name: id_tecnico; Type: DEFAULT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.m_receptores_averias ALTER COLUMN id_tecnico SET DEFAULT nextval('public.receptores_averias_id_tecnico_seq'::regclass);


--
-- Name: id_tecnico; Type: DEFAULT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.m_tecnicos ALTER COLUMN id_tecnico SET DEFAULT nextval('public.tecnicos_id_tecnico_seq'::regclass);


--
-- Name: id_tipo_averia; Type: DEFAULT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.m_tipos_averia ALTER COLUMN id_tipo_averia SET DEFAULT nextval('public.tipos_averia_id_tipo_averia_seq'::regclass);


--
-- Data for Name: averias_abiertas; Type: TABLE DATA; Schema: public; Owner: eduardo
--

COPY public.averias_abiertas (id_averia_abierta, id_averia, ts_inicio, ts_acuse, id_tecnico_acusa) FROM stdin;
\.


--
-- Name: averias_abiertas_id_averia_abierta_seq; Type: SEQUENCE SET; Schema: public; Owner: eduardo
--

SELECT pg_catalog.setval('public.averias_abiertas_id_averia_abierta_seq', 24, true);


--
-- Data for Name: averias_cerradas; Type: TABLE DATA; Schema: public; Owner: eduardo
--

COPY public.averias_cerradas (id_averia_cerrada, id_averia, ts_inicio, ts_acuse, id_tecnico_acusa, ts_fin) FROM stdin;
\.


--
-- Name: averias_cerradas_id_averia_cerrada_seq; Type: SEQUENCE SET; Schema: public; Owner: eduardo
--

SELECT pg_catalog.setval('public.averias_cerradas_id_averia_cerrada_seq', 27, true);


--
-- Name: averias_id_averia_seq; Type: SEQUENCE SET; Schema: public; Owner: eduardo
--

SELECT pg_catalog.setval('public.averias_id_averia_seq', 15, true);


--
-- Data for Name: m_averias; Type: TABLE DATA; Schema: public; Owner: eduardo
--

COPY public.m_averias (id_averia, area, subarea, sistema, elemento, prioridad, nombre, mensaje, descripcion, actuacion, id_tipo_averia, zona) FROM stdin;
9	Pintura	Transportadores	GF1	CR1	0	Tiempo Tansito B1	Se ha superado el tiempo de transito para la llegada al B1	El tiempo de transito del detector mide el tiempo entre que el skid sale del detector anterior y llega al detector actual, si supera un tiempo dado se produce este error	Comprobar si el skid patina sobre los rodillos o bien si esta enganchado mecánicamente o bien si el motor de arrastre tiene algún problema	1	Cota 0
10	Pintura	Transportadores	GF1	MG2	0	Termico Motor 1	Se ha producido el disparo de la protección térmica del motor 1	La protección térmica del motor se ha disparado por una sobretensión producida en el motor. 	Comprobar que el motor no esta bloqueado mecánicamente. 	1	Cota 1
11	Pintura	Maquinas	Laca 1	ESTATICA1	0	Fallo Boquilla 5	El controlador de la boquilla 5 ha dada un fallo	El controlador de la boquilla, controla el nivel de alta tensión de la boquilla así como las revoluciones a las que gira, el fallo puede deberse a un problema en la tensión o bien en el motor de giro de la misma. 	Comprobar que el sistema de alta tensión esta funcionando correctamente y comprobar que el giro de la boquilla no esta bloqueada	1	Zona 1
12	Chapa	Transportadores	GF1	CR1	0	Fallo Ciclo B1	Se ha detectado el detector B1 fuera del ciclo correspondiente	El detector debe ser activado únicamente en las etapas del ciclo designadas, si se detecta en un momento diferentes se activa este error.	Comprobar que el camino de rodillos no esta fuera de ciclo, vaciar la mesa y ciclarla en estado vacío. 	1	Cota 0
13	Chapa	Maquinas	Soldadura Puerta	ROBOT2	0	Fallo Resolver Eje 1	Se ha producido un error en el resolver del eje 1	Cada uno de los seis ejes del robot tiene un resolver que controla su movimiento, en este caso este resolver ha arrojado un error en el resolver del eje 1	Comprobar que el resolver no se ha desplazado mecánicamente y comprobar que el eje no este bloqueado	1	Robots
14	Montaje Final	Infraestructura	Andon Calidad	PUNTO5	0	Fallo modulo I/O	Se ha producido un fallo en el modulo I/O	Cada uno de los puntos de llamada del andon calidad esta compuesto por un modulo I/O este modulo ha dado un fallo 	Comprobar que el modulo I/O esta alimentado y que no hay un problema de red entre el modulo I/O y el controlador	1	Trim
15	Montaje final	Maquinas	Llenado 1	BOMBA3	0	Fallo Variador 	Se ha producido un fallo en el variador de la bomba	Cada bomba esta controlada por un variador, este variador ha producido una salida de fallo.	Comprobar que la bomba no esta bloqueada mecánicamente ni ha entrada aire en su zona de bombeo	1	Bombas
\.


--
-- Data for Name: m_receptores_averias; Type: TABLE DATA; Schema: public; Owner: eduardo
--

COPY public.m_receptores_averias (id_receptor_averias, id_tecnico, topic_receptor) FROM stdin;
1	1	Smartphone 1
2	2	Smartphone 2
\.


--
-- Data for Name: m_tecnicos; Type: TABLE DATA; Schema: public; Owner: eduardo
--

COPY public.m_tecnicos (id_tecnico, nombre, apellido1, apellido2, id_number) FROM stdin;
1	Eduardo	Buetas	Sanjuan	18046256L
2	Juan	Garcia	Garces	98345212Z
\.


--
-- Data for Name: m_tipos_averia; Type: TABLE DATA; Schema: public; Owner: eduardo
--

COPY public.m_tipos_averia (id_tipo_averia, nombre, descripcion) FROM stdin;
1	Digital	Las averías digitales se lanzan por el cambio de un valor digital (1 o 0)
2	Analogica	Las averías analogicas se lanzan cuando un valor entero supera un valor o pasa por debajo de el
\.


--
-- Name: receptores_averias_id_receptor_averias_seq; Type: SEQUENCE SET; Schema: public; Owner: eduardo
--

SELECT pg_catalog.setval('public.receptores_averias_id_receptor_averias_seq', 2, true);


--
-- Name: receptores_averias_id_tecnico_seq; Type: SEQUENCE SET; Schema: public; Owner: eduardo
--

SELECT pg_catalog.setval('public.receptores_averias_id_tecnico_seq', 1, false);


--
-- Name: tecnicos_id_tecnico_seq; Type: SEQUENCE SET; Schema: public; Owner: eduardo
--

SELECT pg_catalog.setval('public.tecnicos_id_tecnico_seq', 2, true);


--
-- Name: tipos_averia_id_tipo_averia_seq; Type: SEQUENCE SET; Schema: public; Owner: eduardo
--

SELECT pg_catalog.setval('public.tipos_averia_id_tipo_averia_seq', 2, true);


--
-- Name: id_averia_abierta_pk; Type: CONSTRAINT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.averias_abiertas
    ADD CONSTRAINT id_averia_abierta_pk PRIMARY KEY (id_averia_abierta);


--
-- Name: id_averia_cerrada_pk; Type: CONSTRAINT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.averias_cerradas
    ADD CONSTRAINT id_averia_cerrada_pk PRIMARY KEY (id_averia_cerrada);


--
-- Name: id_averias_pk; Type: CONSTRAINT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.m_averias
    ADD CONSTRAINT id_averias_pk PRIMARY KEY (id_averia);


--
-- Name: id_receptores_averias_pk; Type: CONSTRAINT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.m_receptores_averias
    ADD CONSTRAINT id_receptores_averias_pk PRIMARY KEY (id_receptor_averias);


--
-- Name: id_tecnicos_pk; Type: CONSTRAINT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.m_tecnicos
    ADD CONSTRAINT id_tecnicos_pk PRIMARY KEY (id_tecnico);


--
-- Name: id_tipo_averia_pk; Type: CONSTRAINT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.m_tipos_averia
    ADD CONSTRAINT id_tipo_averia_pk PRIMARY KEY (id_tipo_averia);


--
-- Name: insert; Type: TRIGGER; Schema: public; Owner: eduardo
--

CREATE TRIGGER insert AFTER INSERT ON public.averias_abiertas FOR EACH ROW EXECUTE PROCEDURE public.tri_insert_averia_abierta();


--
-- Name: insert; Type: TRIGGER; Schema: public; Owner: eduardo
--

CREATE TRIGGER insert AFTER INSERT ON public.averias_cerradas FOR EACH ROW EXECUTE PROCEDURE public.tri_insert_averia_cerrada();


--
-- Name: id_averia_a_abierta_fk; Type: FK CONSTRAINT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.averias_abiertas
    ADD CONSTRAINT id_averia_a_abierta_fk FOREIGN KEY (id_averia) REFERENCES public.m_averias(id_averia);


--
-- Name: id_averia_a_cerrada_fk; Type: FK CONSTRAINT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.averias_cerradas
    ADD CONSTRAINT id_averia_a_cerrada_fk FOREIGN KEY (id_averia) REFERENCES public.m_averias(id_averia);


--
-- Name: id_tecnico_a_abiertas_fk; Type: FK CONSTRAINT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.averias_abiertas
    ADD CONSTRAINT id_tecnico_a_abiertas_fk FOREIGN KEY (id_tecnico_acusa) REFERENCES public.m_tecnicos(id_tecnico);


--
-- Name: id_tecnico_a_cerradas_fk; Type: FK CONSTRAINT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.averias_cerradas
    ADD CONSTRAINT id_tecnico_a_cerradas_fk FOREIGN KEY (id_tecnico_acusa) REFERENCES public.m_tecnicos(id_tecnico);


--
-- Name: id_tecnico_fk; Type: FK CONSTRAINT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.m_receptores_averias
    ADD CONSTRAINT id_tecnico_fk FOREIGN KEY (id_tecnico) REFERENCES public.m_tecnicos(id_tecnico);


--
-- Name: id_tipo_averia_fk; Type: FK CONSTRAINT; Schema: public; Owner: eduardo
--

ALTER TABLE ONLY public.m_averias
    ADD CONSTRAINT id_tipo_averia_fk FOREIGN KEY (id_tipo_averia) REFERENCES public.m_tipos_averia(id_tipo_averia);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

