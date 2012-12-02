-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Client: 127.0.0.1
-- Généré le: Dim 02 Décembre 2012 à 13:20
-- Version du serveur: 5.5.25a-log
-- Version de PHP: 5.3.14

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `takemydata`
--

-- --------------------------------------------------------

--
-- Structure de la table `challenges`
--

CREATE TABLE IF NOT EXISTS `challenges` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` varchar(256) NOT NULL,
  `dataset` int(11) NOT NULL,
  `goal` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `datasets`
--

CREATE TABLE IF NOT EXISTS `datasets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `desc` text NOT NULL,
  `status` int(11) DEFAULT '0',
  `home` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `parsors`
--

CREATE TABLE IF NOT EXISTS `parsors` (
  `id` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `points`
--

CREATE TABLE IF NOT EXISTS `points` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dataset` int(11) NOT NULL,
  `sourced` tinyint(1) NOT NULL,
  `data` text NOT NULL,
  `count` int(11) NOT NULL DEFAULT '0',
  `update` int(11) NOT NULL DEFAULT '0',
  `lat` float NOT NULL,
  `lon` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `rules`
--

CREATE TABLE IF NOT EXISTS `rules` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parsor` int(11) NOT NULL,
  `name` text NOT NULL,
  `description` varchar(32) NOT NULL,
  `type` varchar(16) NOT NULL,
  `data` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pseudo` varchar(32) NOT NULL,
  `passwd` varchar(42) NOT NULL,
  `email` varchar(64) NOT NULL,
  `access` tinyint(4) NOT NULL,
  `perms` smallint(6) NOT NULL,
  `total` int(11) NOT NULL,
  `points` text NOT NULL,
  `challengs` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='Liste des utilisateurs' AUTO_INCREMENT=3 ;

--
-- Contenu de la table `users`
--

INSERT INTO `users` (`id`, `pseudo`, `passwd`, `email`, `access`, `perms`, `total`, `points`, `challengs`) VALUES
(1, 'Guest', 'e0a2c020d17e7be5701d2d36a8e96b662ff68904', '', 0, 0, 0, '', ''),
(2, 'Admin', '9145a5175f7fcdbfe45f222a944b368bedf30286', 'admin@tmd.fr', 1, 7, 0, '', '');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
