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

class Text {

    /**
     * @var array Used to convert BBCode into HTML
     */
    private static $bbcoder = null;

    /**
     * @var array All the translations for a given text
     */
    private $translations = array();

    /**
     * Instanciate a Text in the default user language
     * 
     * @param string $text The text
     */
    public function __construct($text) {
        $this->translations = array(LANGUAGE => utf8_decode($text));
    }

    /**
     * Add or modify a translation in a given language for the current Text
     * @param type $lang The choosen language
     * @param type $text The traduction
     */
    public function addTranslation($lang, $text) {
        $this->translations[$lang] = utf8_decode($text);
    }

    /**
     * Retreive the translation on the current text in the given language
     * 
     * @param string $lang The language
     * @return string The translate text 
     */
    public function get($lang = LANGUAGE) {
        if (!empty($this->translations[$lang])) {
            return ($this->translations[$lang]);
        } else {
            return ($this->translations[DEFAULT_LANGUAGE]);
        }
    }

    /**
     * HTML encode a given string
     * 
     * @param string $text The string to encode
     * @param boolean $encoded True if the text is utf8_encode
     * @return string The encoded string 
     */
    public static function e($text, $encoded = false) {
        if ($encoded) {
            return (htmlentities(utf8_decode($text)));
        } else {
            return (htmlentities($text));
        }
    }

    /**
     * Transform a given BBcoded string into HTML
     * 
     * @param string $text The string to encode
     * @return string The decoded HTML string 
     */
    public static function bb($text) {
        if (self::$bbcoder === null) {
            self::initBBCoder();
        }
        return (nl2br(preg_replace(array_keys(self::$bbcoder), array_values(self::$bbcoder), $text)));
    }

    /**
     * Initialize BBCode decoder 
     */
    private static function initBBCoder() {
        self::$bbcoder = array(
            '#\[i\](.*?)\[/i\]#is' => '<em>$1</em>',
            '#\[b\](.*?)\[/b\]#is' => '<strong>$1</strong>',
            '#\[u\](.*?)\[/u\]#is' => '<u>$1</u>',
            '#\[url_ext=(.*?)(:[a-z0-9]*?)?\](.*?)\[/url_ext\]#is' => '<a href="$1" target="_blank">$3</a>',
            '#\[img float=(.*?) width=(.*?) height=(.*?) title="(.*?)"\](.*?)\[/img\]#is' => '<a class="photo f$1" href="$5" title="$4"><img src="$5" width="$2" alt="$4" height="$3" /></a>'
        );
    }

    public static function stripAccents($str) {
        $i = 0;
        $len = strlen($str);

        while ($i < $len) {
            $c = $str[$i];
            if (!(($c >= 'a' && $c <= 'z') || ($c >= 'A' && $c <= 'Z') || ($c >= '0' && $c <= '9'))) {
                $str[$i] = '%';
            }
            ++$i;
        }
        return ($str);
    }

}
