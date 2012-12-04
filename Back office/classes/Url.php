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

class Url {

    /**
     * Static class
     */
    private function __construct() {
        
    }

    /**
     * Convert a basic string into its url equivalent
     * 
     * @param string $base_url The base string
     * @param boolean $encoded True if base_url is utf8_encode
     * @return string The URL
     */
    public static function URLize($base_url, $encoded = false) {
        if ($encoded) {
            $base_url = htmlentities(html_entity_decode(utf8_decode($base_url)));
        } else {
            $base_url = htmlentities(html_entity_decode($base_url));
        }
        $base_url = str_replace('&amp;', '', $base_url);
        $base_url = preg_replace('#&([a-zA-Z]{1})([a-z]+);#', '$1', $base_url);
        $base_url = preg_replace('#[^a-zA-Z0-9 \-]{1}#', '', $base_url);
        $base_url = str_replace(' ', '-', trim(strtolower($base_url)));
        $base_url = preg_replace('#[\-]+#', '-', $base_url);
        return ($base_url);
    }

    public static function fromWStoHome() {
        $base = rtrim(BASE_ROOT, '/');
        return (substr($base, 0, strlen($base) - 2));
    }

    public static function toHome() {
        return (BASE_ROOT . '/panel');
    }

    public static function toConnection() {
        return (BASE_ROOT . '/connexion');
    }

    public static function toDatasets() {
        return (BASE_ROOT . '/datasets');
    }

    public static function toDatasetsCreate() {
        return (BASE_ROOT . '/datasets/create');
    }

    public static function toEditParsor($parsor) {
        return (BASE_ROOT . '/parsor/edit/' . $parsor);
    }

    public static function toDatasetImport($parsor) {
        return (BASE_ROOT . '/datasets/import/' . $parsor);
    }

    public static function toEditParsorRule($parsor, $rule) {
        return (BASE_ROOT . '/editrule/' . $parsor . '/' . $rule);
    }

    public static function toPoints($parsor) {
        return (BASE_ROOT . '/parsor/points/' . $parsor);
    }

    public static function toHomePage() {
        return (BASE_ROOT . '/homepage');
    }

    public static function toUsers() {
        return (BASE_ROOT . '/users');
    }

    public static function toEditPasswd($user) {
        return (BASE_ROOT . '/users/passwd/' . $user);
    }

    public static function toDelUser($user) {
        return (BASE_ROOT . '/users/del/' . $user);
    }

    public static function toUserCreate() {
        return (BASE_ROOT . '/users/create');
    }

    public static function toExport($type, $dataset) {
        return (BASE_ROOT . '/export/' . $type . '/' . $dataset);
    }

}