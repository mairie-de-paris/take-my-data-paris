<?php

/**
 * Copyright 2012-2013 The Apache Software Foundation
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

defined('EXEC') or die('Access forbidden');

class User {

    private static $SESSINDEX = 'tmdsessusr';

    /**
     * @var User Singleton
     */
    private static $instance = null;

    /**
     * @var int User ID
     */
    private $id = 0;

    /**
     * @var string Username
     */
    private $uname = null;

    /**
     * @var int User access level
     */
    private $access = GUEST;
    private $grade = 0;
    private $total = 0;

    /**
     * @var int User permissions
     */
    private $perms = NO_RIGHT;
    private $challengs = array();
    private $points = array();

    public static function getQueryFields() {
        return (new QueryDescriptor('users',
                        array('id' => 'UID',
                            'pseudo' => 'Upseudo',
                            'passwd' => 'Upasswd',
                            'access' => 'Uaccess',
                            'email' => 'Umail',
                            'points' => 'Upoints',
                            'challengs' => 'Uchalls',
                            'total' => 'Utotal',
                            'perms' => 'Uperms')));
    }

    /**
     * Instanciate a User from a database row
     * 
     * $id -> `UID`
     * $uname -> `Upseudo`
     * $access -> `Uaccess`
     * $perms -> `Uperms`
     * 
     * @param stdClass $row A database row
     */
    private function __construct(stdClass $row) {
        $this->id = intval($row->UID);
        $this->uname = Text::e($row->Upseudo);
        $this->access = intval($row->Uaccess);
        if (!empty($row->Upoints)) {
            $this->points = unserialize($row->Upoints);
        }
        if (!empty($row->Uchalls)) {
            $this->challengs = unserialize($row->Uchalls);
        }
        $this->perms = intval($row->Uperms);
        $this->total = intval($row->Utotal);
        $this->grade = intval($row->grade);
    }

    /**
     * Singleton
     * 
     * @return User Instance
     */
    public static function getInstance() {
        if (self::$instance === null) {
            if (!empty($_SESSION[self::$SESSINDEX])) {
                self::$instance = unserialize($_SESSION[self::$SESSINDEX]);
            } else {
                self::$instance = User::connect('Guest', 'guest');
                self::$instance->perms = NO_RIGHT;
            }
        }
        return (self::$instance);
    }

    public static function getAll($access = 1) {
        $list = array();
        $query = 'SELECT ';
        $infos = self::getQueryFields();
        $first = true;
        foreach ($infos->getColumns() as $col => $val) {
            if ($first) {
                $first = false;
            } else {
                $query .= ', ';
            }
            $query .= '`user_data`.`' . $col . '` AS `' . $val . '`';
        }
        $query .= ', (
            SELECT COUNT(*)
            FROM `' . $infos->getTable() . '` AS `tmp`
            WHERE `tmp`.`total` <= `user_data`.`total`
            AND `tmp`.`access` = 0
            AND `tmp`.`email` != "") AS `grade`';
        $query .= ' FROM `' . $infos->getTable() . '` AS `user_data`';
        $query .= ' WHERE `access` = ' . intval($access);

        $res = DBO::query($query);
        while (($row = $res->fetch_object())) {
            $list[] = new User($row);
        }
        return ($list);
    }

    public static function getFromID($id) {
        $query = 'SELECT ';
        $infos = self::getQueryFields();
        $first = true;
        foreach ($infos->getColumns() as $col => $val) {
            if ($first) {
                $first = false;
            } else {
                $query .= ', ';
            }
            $query .= '`user_data`.`' . $col . '` AS `' . $val . '`';
        }
        $query .= ', (
            SELECT COUNT(*)
            FROM `' . $infos->getTable() . '` AS `tmp`
            WHERE `tmp`.`total` <= `user_data`.`total`
            AND `tmp`.`access` = 0
            AND `tmp`.`email` != "") AS `grade`';
        $query .= ' FROM `' . $infos->getTable() . '` AS `user_data`';
        $query .= ' WHERE `id` = ' . intval($id);

        $res = DBO::query($query);
        if (($row = $res->fetch_object())) {
            return (new User($row));
        }
        return (null);
    }

    /**
     * Try an connect a User to the site
     * @param string $pseudo Username
     * @param string $passwd Password
     * @return User The User
     */
    public static function connect($pseudo, $passwd) {
        $query = 'SELECT ';
        $infos = self::getQueryFields();
        $first = true;
        foreach ($infos->getColumns() as $col => $val) {
            if ($first) {
                $first = false;
            } else {
                $query .= ', ';
            }
            $query .= '`user_data`.`' . $col . '` AS `' . $val . '`';
        }
        $query .= ', (
            SELECT COUNT(*)
            FROM `' . $infos->getTable() . '` AS `tmp`
            WHERE `tmp`.`total` <= `user_data`.`total`
            AND `tmp`.`access` = 0
            AND `tmp`.`email` != "") AS `grade`';
        $query .= ' FROM `' . $infos->getTable() . '` AS `user_data`';
        $query .= ' WHERE `pseudo` = ' . DBO::escape($pseudo) . ' AND `passwd` = ' . DBO::escape(sha1(md5($pseudo) . $passwd . '!#!'));
        $query .= ' LIMIT 1;';


        $res = DBO::query($query);
        if (($row = $res->fetch_object())) {
            self::$instance = new User($row);
            self::store();
        } else {
            self::$instance = null;
        }
        return (self::$instance);
    }

    /**
     * Create a new User in the database
     * 
     * @param array $data The date source
     * @return bool The query result
     */
    public static function create(array $data) {
        $query = 'INSERT INTO `' . self::getQueryFields()->getTable() . '`
            (`pseudo`, `passwd`, `email`, `access`, `perms`, `points`, `challengs`, `total`)
            VALUES
            (' . DBO::escape($data['Upseudo']) . ',
             ' . DBO::escape(sha1(md5($data['Upseudo']) . $data['Upasswd'] . '!#!')) . ',
             ' . DBO::escape($data['Umail']) . ',
             ' . intval($data['Uaccess']) . ',
             ' . intval($data['Uperms']) . ',
            "", "", 0);';
        return (DBO::query($query));
    }

    public static function changePasswd(array $data) {
        $user = User::getFromID($data['UID']);
        if (!$user) {
            return (false);
        }
        $query = 'UPDATE `' . self::getQueryFields()->getTable() . '`
            SET `passwd` = ' . DBO::escape(sha1(md5($user->getUname()) . $data['Upasswd'] . '!#!')) . '
            WHERE `id` = ' . intval($data['UID']);
        DBO::query($query);
        return (true);
    }

    public static function delete(array $data) {
        $query = 'DELETE FROM `' . self::getQueryFields()->getTable() . '` WHERE `id` = ' . intval($data['UID']);
        return (DBO::query($query));
    }

    /**
     * Disconnect a User
     */
    public static function disconnect() {
        self::$instance = new User();
        self::$instance->perms = NO_RIGHT;
        self::store();
    }

    /**
     * Save User instance
     */
    public static function store() {
        if (self::$instance !== null) {
            $_SESSION[self::$SESSINDEX] = serialize(self::$instance);
        }
    }

//
// RIGHTS MANAGMENT
//

    public function isAdmin() {
        return ($this->access >= ADMIN);
    }

    public function can($action) {
        return (($this->perms & $action) === $action);
    }

    public static function emailExist($email) {
        $query = 'SELECT `id` AS `UID` FROM `' . self::getQueryFields()->getTable() . '`';
        $query .= ' WHERE `email` = ' . DBO::escape($email);

        $res = DBO::query($query);
        if ($res->fetch_object()) {
            return (true);
        }
        return (false);
    }

    /**
     * Verify User password
     * 
     * @param string $passwd The User password
     * @return bool Verification result
     */
    public function verifyPassword($passwd) {
        $passwd = sha1(md5($this->uname) . $passwd . '!#!');
        $query = 'SELECT `id` AS `UID` FROM `' . self::getQueryFields()->getTable() . '`';
        $query .= ' WHERE `passwd` = ' . DBO::escape($passwd) . ' AND `pseudo` = ' . DBO::escape($this->uname);
        $res = DBO::query($query);
        if (($row = $res->fetch_object())) {
            return (true);
        }
        return (false);
    }

    public function getUname() {
        return ($this->uname);
    }

    public function getID() {
        return ($this->id);
    }

    public function getObject() {
        $data = array('id' => $this->id);
        foreach ($this->points as $dataset => $points) {
            $data['points'][] = (object) array(
                        'id_type' => intval($dataset),
                        'nb' => count($points),
            );
        }

        $data['grade'] = (object) array(
                    'name' => $this->uname,
                    'grade' => $this->grade,
                    'nb_points' => $this->total
        );

        $data['challenges'] = array();
        foreach ($this->challengs as $chall) {
            $data['challenges'][] = (object) array(
                        'id' => intval($chall->id),
                        'nb' => intval($chall->nb),
            );
        }
        $data['medals'] = array();
        return ((object) $data);
    }

    public function getPoints() {
        return ($this->points);
    }

    public function addPoint($dataset, $point) {
        if (!isset($this->points[$dataset])) {
            $this->points[$dataset] = array($point);
            $this->total += 1;
            return (true);
        } else {
            foreach ($this->points[$dataset] as $apoint) {
                if ($point === $apoint) {
                    return (false);
                }
            }
            $this->points[$dataset][] = $point;
        }
        $this->total += 1;
        return (true);
    }

    public function update() {
        $query = 'UPDATE ' . self::getQueryFields()->getTable() . '
            SET `points` = ' . DBO::escape(serialize($this->points)) . ',
                `total` = ' . intval($this->total) . '
            WHERE `id` = ' . intval($this->id);
        DBO::query($query);
    }

}
