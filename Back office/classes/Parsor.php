<?php

/**
 * Copyright 2012-2013 Mairie de Paris
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

abstract class Parsor {

    abstract public function checkSource($source = null);

    abstract public function getColumns();

    abstract public function getRows();

    abstract public function getCol($row, $col);

    protected $rules = array();
    protected $source = null;

    public function addRule(stdClass $row) {
        $this->rules[] = new Rule($row);
    }

    /**
     * Retourne le nom du type de parseur en fonction de son ID
     * 
     * @param int $type L'ID du type
     * @return string Le nom du type
     */
    public static function getFromType($type) {
        $types = self::getTypes();
        if (!emtpy($types[$type])) {
            return ($types[$type]);
        } else {
            die('Unknow ' . $type . ' in ' . __FILE__ . ' at line ' . (__LINE__ - 1));
        }
    }

    public static function getTypes() {
        return (array(
            1 => 'CSV',
                //2 => 'Google Fusion Table'
                ));
    }

    public static function getParsors() {
        return (array(
            1 => 'CSVParsor',
                //2 => 'FusionTableParsor'
                ));
    }

    public static function getParsor($type) {
        $parsors = self::getParsors();
        if (!empty($parsors[$type])) {
            return ($parsors[$type]);
        } else {
            die('Unknow ' . $type . ' in ' . __FILE__ . ' at line ' . (__LINE__ - 1));
        }
    }

    protected function getURL($url) {
        $core = curl_init($url);
        curl_setopt($core, CURLOPT_RETURNTRANSFER, 1);
        $res = curl_exec($core);
        curl_close($core);
        return ($res);
    }

    public static function getFromID($id) {
        $query = 'SELECT ';
        $rules = Rule::getQueryFields();
        foreach ($rules->getColumns() as $col => $as) {
            $query .= '`' . $rules->getTable() . '`.`' . $col . '` AS `' . $as . '`, ';
        }
        $query .= '`parsors`.* FROM `parsors`';
        $query .= ' LEFT JOIN `' . $rules->getTable() . '` ON `parsors`.`id` = `' . $rules->getTable() . '`.`parsor`';
        $query .= ' WHERE `parsors`.`id` = ' . intval($id);

        $res = DBO::query($query);
        if (($row = $res->fetch_object())) {
            $parsorName = Parsor::getParsor(intval($row->type));
            if (!empty($row->url)) {
                $parsor = new $parsorName($row->url);
            } else {
                $parsor = new $parsorName('');
            }
            $parsor->addRule($row);
            while (($row = $res->fetch_object())) {
                $parsor->addRule($row);
            }
            return ($parsor);
        }
        return (null);
    }

    public function getRules() {
        return ($this->rules);
    }

    public function setSource($source) {
        $this->source = $source;
    }

    public static function create(array $source) {
        $query = 'INSERT INTO `parsors`
            (`id`, `type`)
            VALUES
            (' . intval($source['id']) . ',
            ' . intval($source['type']) . ');';

        DBO::query($query);

        foreach ($source['fieldname'] as $key => $name) {
            if (!empty($name) && !empty($source['fieldtype'][intval($key)]) && !empty($source['fielddesc'][intval($key)])) {
                $query = 'INSERT INTO `rules`
                    (`parsor`, `name`, `description`, `type`, `data`)
                    VALUES
                    (' . intval($source['id']) . ', 
                     ' . DBO::escape($name) . ', 
                     ' . DBO::escape($source['fielddesc'][intval($key)]) . ', 
                     ' . DBO::escape($source['fieldtype'][intval($key)]) . ', 
                     ' . DBO::escape(serialize(array())) . ')';
                DBO::query($query);
            }
        }

        return (true);
    }

    public static function import(array $source) {
        $parsor = Parsor::getFromID($source['id']);
        $parsor->setSource(urldecode($source['source']));

        foreach ($parsor->getRows() as $row) {
            $data = array();

            foreach ($_POST['rule'] as $ruleID => $col) {
                $rule = $parsor->getRule($ruleID);
                $line = $parsor->getCol($row, $col);
                $choices = $rule->getData();

                if (empty($choices)) {
                    $data[$ruleID] = $line;
                } else {
                    $data[$ruleID] = array();
                    foreach ($choices as $choice) {
                        $num = 0;
                        if ($line === $choice) {
                            $num = 1;
                        }
                        $data[$ruleID][$choice] = $num;
                    }
                }
            }
            Point::create($_POST['id'], floatval($parsor->getCol($row, $_POST['lat'])), floatval($parsor->getCol($row, $_POST['lon'])), $data);
        }
        return (true);
    }

    public function getRule($id) {
        foreach ($this->rules as $rule) {
            if ($rule->getID() === $id) {
                return ($rule);
            }
        }
        return (null);
    }

}