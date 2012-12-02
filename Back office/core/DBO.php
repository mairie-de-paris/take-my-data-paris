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

class DBO {

    /**
     * @var mysqli MySQLi database object 
     */
    private $dbo = null;

    /**
     * @var DBO singleton 
     */
    private static $instance = null;

    /**
     * Initialize the connection with the database engine 
     */
    protected function __construct() {
        global $__host, $__username, $__password, $__dbname;

        $this->dbo = new mysqli($__host, $__username, $__password, $__dbname);
    }

    /**
     * Execute a non-prepared statement to the database
     * 
     * @param string $query The query to execute
     * @return mixed false if an error occurs, a value depending on the query otherwise 
     */
    public static function query($query) {
        if (!self::$instance) {
            self::$instance = new DBO();
        }
        return (self::$instance->executeQuery($query));
    }

    /**
     * Send the query to the MySQL engine
     * 
     * @param string $query The query to execute
     * @return mixed false if an error occurs, a value depending on the query otherwise 
     */
    private function executeQuery($query) {
        $res = $this->dbo->query($query);
        if (!$res) {
            if (DEBUG) {
                echo '<pre>' . $query . '</pre>';
                Exed::e($this->dbo->error);
            }
            return (false);
        } else if (strpos(strtoupper($query), 'INSERT') === 0) {
            return ($this->dbo->insert_id);
        } else if (strpos(strtoupper($query), 'SELECT') === 0) {
            return ($res);
        } else {
            return ($this->dbo->affected_rows);
        }
    }

    /**
     * Escape a string and add quotes around it
     *
     * @param string $string The string to escape
     * @param boolean $must_encode True if the string is in utf8
     * @return string The escaped string 
     */
    public static function escape($string, $must_encode = false) {
        return ('"' . self::protect($string, $must_encode) . '"');
    }

    /**
     * Escape a string
     *
     * @param string $string The string to escape
     * @param boolean $must_encode True if the string is in utf8
     * @return string The escaped string 
     */
    public static function protect($string, $must_encode = false) {
        if (!self::$instance) {
            self::$instance = new DBO();
        }
        if ($must_encode) {
            $string = utf8_decode($string);
        }
        return (self::$instance->dbo->real_escape_string($string));
    }

}
